package com.devrelease.service;

import com.devrelease.dto.request.RegisterRequest;
import com.devrelease.dto.response.AuthResponse;
import com.devrelease.dto.request.LoginRequest;
import com.devrelease.enums.Role;
import com.devrelease.exception.UnauthorizedException;
import com.devrelease.model.User;
import com.devrelease.repository.UserRepository;
import com.devrelease.security.JwtUtil;
import com.devrelease.security.UserDetailsServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                       UserDetailsServiceImpl userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UnauthorizedException("Email already in use");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.DEVELOPER);
        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtUtil.generateToken(userDetails);
        return buildResponse(token, user);
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new UnauthorizedException("Invalid credentials");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtUtil.generateToken(userDetails);
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        return buildResponse(token, user);
    }

    private AuthResponse buildResponse(String token, User user) {
        return new AuthResponse(token,
                new AuthResponse.UserDto(user.getId(), user.getName(), user.getEmail(), user.getRole().name()));
    }
}
