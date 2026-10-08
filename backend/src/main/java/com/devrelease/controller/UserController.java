package com.devrelease.controller;

import com.devrelease.dto.request.UpdateProfileRequest;
import com.devrelease.dto.response.AuthResponse;
import com.devrelease.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse.UserDto> me(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(userService.getProfile(user.getUsername()));
    }

    @PutMapping("/me")
    public ResponseEntity<AuthResponse.UserDto> update(@AuthenticationPrincipal UserDetails user,
                                                       @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(userService.updateProfile(user.getUsername(), request));
    }

    @GetMapping("/search")
    public ResponseEntity<AuthResponse.UserDto> searchByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @GetMapping
    public ResponseEntity<List<AuthResponse.UserDto>> listAll(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(userService.listAllUsers(user.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        userService.deleteUser(id, user.getUsername());
        return ResponseEntity.noContent().build();
    }
}
