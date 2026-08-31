package com.example.auth_demo.controller;

import com.example.auth_demo.dto.AuthDTOs.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication Management", description = "Endpoints for user onboarding, login, logout, refresh tokens, and recovery")
public class AuthController {

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Accepts user details and registers a new account dynamically")
    @ApiResponse(responseCode = "201", description = "User successfully registered")
    @ApiResponse(responseCode = "400", description = "Invalid request or missing required fields")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank() ||
            request.getUsername() == null || request.getUsername().isBlank() ||
            request.getPassword() == null || request.getPassword().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Email, username, and password are required"));
        }

        String generatedId = "usr_" + UUID.randomUUID().toString().substring(0, 8);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
            "message", "User registered successfully",
            "userId", generatedId,
            "username", request.getUsername(),
            "email", request.getEmail()
        ));
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Accepts any user credentials and returns dynamic tokens")
    @ApiResponse(responseCode = "200", description = "Successfully authenticated")
    @ApiResponse(responseCode = "400", description = "Missing input")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request.getUsernameOrEmail() == null || request.getUsernameOrEmail().isBlank() ||
            request.getPassword() == null || request.getPassword().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Username/email and password are required"));
        }

        String userSlug = request.getUsernameOrEmail().replaceAll("\\s+", "_");
        TokenResponse tokens = new TokenResponse(
            "token_access_" + userSlug + "_" + UUID.randomUUID().toString().substring(0, 8),
            "token_refresh_" + userSlug + "_" + UUID.randomUUID().toString().substring(0, 8)
        );

        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String bearerToken) {
        return ResponseEntity.ok(Map.of("message", "Token invalidated and user successfully logged out."));
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh access token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        if (request.getRefreshToken() != null && !request.getRefreshToken().isBlank()) {
            TokenResponse newTokens = new TokenResponse(
                "token_access_refreshed_" + UUID.randomUUID().toString().substring(0, 8),
                request.getRefreshToken()
            );
            return ResponseEntity.ok(newTokens);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid refresh token"));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Request password reset")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Email is required"));
        }
        String resetToken = "reset_" + UUID.randomUUID().toString().substring(0, 12);
        return ResponseEntity.ok(Map.of(
            "message", "Password reset token sent to " + request.getEmail(),
            "resetToken", resetToken
        ));
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password using reset token")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        if (request.getResetToken() == null || request.getResetToken().isBlank() ||
            request.getNewPassword() == null || request.getNewPassword().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Reset token and new password are required"));
        }
        return ResponseEntity.ok(Map.of("message", "Password updated successfully using token: " + request.getResetToken()));
    }
}