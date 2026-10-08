package com.devrelease.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private UserDto user;
    
    @Data
    @AllArgsConstructor
    public static class UserDto {
        private Long id;
        private String name;
        private String email;
        private String role;
    }
}
