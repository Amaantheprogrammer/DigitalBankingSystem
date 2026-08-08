package com.MyProject.DigitalBankingSystem.auth.service;

import com.MyProject.DigitalBankingSystem.audit.annotation.Auditable;
import com.MyProject.DigitalBankingSystem.audit.entity.AuditLog;
import com.MyProject.DigitalBankingSystem.audit.entity.AuditLogStatus;
import com.MyProject.DigitalBankingSystem.audit.entity.AuditableAction;
import com.MyProject.DigitalBankingSystem.audit.entity.EntityType;
import com.MyProject.DigitalBankingSystem.audit.repository.AuditLogRepository;
import com.MyProject.DigitalBankingSystem.auth.jwt.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.MyProject.DigitalBankingSystem.auth.dto.AuthResponse;
import com.MyProject.DigitalBankingSystem.auth.dto.LoginRequest;
import com.MyProject.DigitalBankingSystem.auth.jwt.JwtService;
import com.MyProject.DigitalBankingSystem.exception.DuplicateResourceException;
import com.MyProject.DigitalBankingSystem.exception.ResourceNotFoundException;
import com.MyProject.DigitalBankingSystem.user.dto.UserRequest;
import com.MyProject.DigitalBankingSystem.user.entity.Role;
import com.MyProject.DigitalBankingSystem.user.entity.User;
import com.MyProject.DigitalBankingSystem.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (Exception ex) {
            auditLogRepository.save(
                    AuditLog.builder()
                            .userEmail(request.getEmail())
                            .action(AuditableAction.LOGIN)
                            .entityType(EntityType.AUTH)
                            .status(AuditLogStatus.FAILURE)
                            .timestamp(LocalDateTime.now())
                            .build()
            );

            throw ex;
        }
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email " + request.getEmail()));
        String token = jwtService.generateToken(request.getEmail(), user.getRole().name());
        auditLogRepository.save(
                AuditLog.builder()
                        .userEmail(request.getEmail())
                        .action(AuditableAction.LOGIN)
                        .entityType(EntityType.AUTH)
                        .entityId(user.getId())
                        .status(AuditLogStatus.SUCCESS)
                        .timestamp(LocalDateTime.now())
                        .build());

        return new AuthResponse(token);
    }

    @Transactional
    public void register(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            auditLogRepository.save(
                    AuditLog.builder()
                            .userEmail(request.getEmail())
                            .action(AuditableAction.REGISTER)
                            .entityType(EntityType.AUTH)
                            .status(AuditLogStatus.FAILURE)
                            .timestamp(LocalDateTime.now())
                            .build()
            );
            throw new DuplicateResourceException("User already exists with email: " + request.getEmail());
        }
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(user);
        auditLogRepository.save(
                AuditLog.builder()
                        .userEmail(request.getEmail())
                        .action(AuditableAction.REGISTER)
                        .entityType(EntityType.AUTH)
                        .entityId(user.getId())
                        .status(AuditLogStatus.SUCCESS)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @Transactional
    @Auditable(action = AuditableAction.LOGOUT, entityType = EntityType.AUTH)
    public void logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        String token = authHeader.substring(7);
        long remainingExpiration = jwtService.getRemainingExpiration(token);
        tokenBlacklistService.blacklistToken(token, remainingExpiration);
    }

}
