package com.devrelease;

import com.devrelease.enums.Role;
import com.devrelease.model.User;
import com.devrelease.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DevReleaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevReleaseApplication.class, args);
    }

    @Bean
    public CommandLineRunner seedDefaultAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                User admin = new User();
                admin.setName("System Admin");
                admin.setEmail("admin@devrelease.io");
                admin.setPassword(passwordEncoder.encode("Admin@123"));
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
                System.out.println("Default ADMIN seeded: admin@devrelease.io / Admin@123");
            }
        };
    }
}
