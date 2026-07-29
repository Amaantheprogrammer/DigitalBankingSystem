package com.MyProject.DigitalBankingSystem.auth.controller;

import com.MyProject.DigitalBankingSystem.auth.dto.AuthResponse;
import com.MyProject.DigitalBankingSystem.auth.dto.LoginRequest;
import com.MyProject.DigitalBankingSystem.auth.service.AuthService;
import com.MyProject.DigitalBankingSystem.user.dto.UserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserRequest request) {
        authService.register(request);
        return ResponseEntity.noContent().build();
    }
}
