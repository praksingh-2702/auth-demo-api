package com.example.auth_demo.controller;

import com.example.auth_demo.dto.AuthDTOs.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Profile Management", description = "Endpoints for testing profile details and account security")
public class UserController {

    @GetMapping("/me")
    @Operation(summary = "Get current user profile (Mock)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getCurrentUser(
            @RequestHeader(value = "Authorization", required = false) String bearerToken,
            @RequestParam(defaultValue = "test_user") String username,
            @RequestParam(defaultValue = "test@example.com") String email,
            @RequestParam(defaultValue = "Test User") String fullName,
            @RequestParam(defaultValue = "ROLE_USER") String role) {

        String dynamicId = "usr_" + UUID.randomUUID().toString().substring(0, 8);
        return ResponseEntity.ok(new UserProfileResponse(dynamicId, username, email, fullName, role));
    }

    @PutMapping("/me")
    @Operation(summary = "Update current user profile (Mock)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> updateProfile(
            @RequestHeader(value = "Authorization", required = false) String bearerToken,
            @RequestBody UpdateProfileRequest request) {

        return ResponseEntity.ok(Map.of(
            "message", "Profile updated successfully (Mock)",
            "updatedName", request.getFullName() != null ? request.getFullName() : "No change",
            "updatedEmail", request.getEmail() != null ? request.getEmail() : "No change"
        ));
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change password for logged-in user (Mock)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> changePassword(
            @RequestHeader(value = "Authorization", required = false) String bearerToken,
            @RequestBody ChangePasswordRequest request) {

        if (request.getOldPassword() == null || request.getOldPassword().isBlank() ||
            request.getNewPassword() == null || request.getNewPassword().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Both old and new passwords are required"));
        }

        return ResponseEntity.ok(Map.of("message", "Password changed successfully (Mock)"));
    }
}