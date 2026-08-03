package com.MyProject.DigitalBankingSystem.auth.service;

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

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()
        ));
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email " + request.getEmail()));
        String token = jwtService.generateToken(request.getEmail(), user.getRole().name());
        return new AuthResponse(token);
    }

    @Transactional
    public void register(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new DuplicateResourceException("User already exists with email: " + userRequest.getEmail());
        }
        User user = User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(user);
    }

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
