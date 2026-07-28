package com.MyProject.DigitalBankingSystem.admin;

import com.MyProject.DigitalBankingSystem.user.entity.Role;
import com.MyProject.DigitalBankingSystem.user.entity.User;
import com.MyProject.DigitalBankingSystem.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeeder {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @PostConstruct
    public void seedAdmin() {
        if (!userRepository.existsByEmail("admin@bank.com")) {
            User admin = User.builder()
                    .name("Amaan")
                    .email("admin@bank.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ROLE_ADMIN)
                    .build();
            userRepository.save(admin);
        }
    }
}
