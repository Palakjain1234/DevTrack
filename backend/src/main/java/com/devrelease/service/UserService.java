package com.devrelease.service;
import com.devrelease.dto.request.UpdateProfileRequest;
import com.devrelease.dto.response.AuthResponse;
import com.devrelease.enums.Role;
import com.devrelease.exception.ResourceNotFoundException;
import com.devrelease.exception.UnauthorizedException;
import com.devrelease.model.User;
import com.devrelease.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse.UserDto getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return toDto(user);
    }

    public AuthResponse.UserDto findByEmail(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with that email"));
        return toDto(user);
    }

    public AuthResponse.UserDto updateProfile(String email, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (request.getName() != null) user.setName(request.getName());
        if (request.getPassword() != null) user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return toDto(user);
    }

    public List<AuthResponse.UserDto> listAllUsers(String requesterEmail) {
        User requester = userRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (requester.getRole() != Role.ADMIN) throw new UnauthorizedException("Admin only");
        return userRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public void deleteUser(Long userId, String requesterEmail) {
        User requester = userRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (requester.getRole() != Role.ADMIN) throw new UnauthorizedException("Admin only");
        userRepository.deleteById(userId);
    }

    private AuthResponse.UserDto toDto(User u) {
        return new AuthResponse.UserDto(u.getId(), u.getName(), u.getEmail(), u.getRole().name());
    }
}
