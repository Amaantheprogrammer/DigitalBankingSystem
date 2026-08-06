package com.MyProject.DigitalBankingSystem.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MyProject.DigitalBankingSystem.user.dto.UpdateUserRequest;
import com.MyProject.DigitalBankingSystem.user.dto.UserResponse;
import com.MyProject.DigitalBankingSystem.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Users", description = "User APIs")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Get user by ID",
            description = "Returns user details by ID"
    )
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        log.info("Fetching user with ID: {}", userId);
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @Operation(
            summary = "Get user by email",
            description = "Returns user details by email"
    )
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        log.info("Fetching user with email: {}", email);
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @Operation(
            summary = "Get user's own detauls",
            description = "Returns user details by of the authenticated user"
    )
    @GetMapping("/my-user")
    public ResponseEntity<UserResponse> getMyProfile() {
        log.info("Fetching your user details");
        return ResponseEntity.ok(userService.getMyProfile());
    }

    @Operation(
            summary = "Update user by ID",
            description = "Update user details by ID"
    )
    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long userId, @Valid @RequestBody UpdateUserRequest request) {
        log.info("Updating user");
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }
}
