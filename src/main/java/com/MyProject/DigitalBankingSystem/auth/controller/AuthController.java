package com.MyProject.DigitalBankingSystem.auth.controller;

import com.MyProject.DigitalBankingSystem.auth.dto.AuthResponse;
import com.MyProject.DigitalBankingSystem.auth.dto.LoginRequest;
import com.MyProject.DigitalBankingSystem.auth.service.AuthService;
import com.MyProject.DigitalBankingSystem.user.dto.UserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication APIs")
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Operation(
            description = "User sign-in"
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(
            description = "User sign-up"
    )
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserRequest request) {
        authService.register(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            description = "User sign-out"
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        authService.logout(request);
        return ResponseEntity.noContent().build();
    }
}
