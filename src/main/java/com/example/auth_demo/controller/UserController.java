package com.example.auth_demo.controller;

import com.example.auth_demo.dto.*;
import com.example.auth_demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Profile Management", description = "Endpoints for testing profile details and account security")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user profile")
    public ResponseEntity<UserResponse> getCurrentUser(@RequestParam(defaultValue = "customer1") String username) {
        return ResponseEntity.ok(userService.getUserProfile(username));
    }

    @PutMapping("/me")
    @Operation(summary = "Update current user profile")
    public ResponseEntity<UserResponse> updateCurrentUser(
            @RequestParam(defaultValue = "customer1") String username,
            @RequestBody CustomerKycRequest request) {
        return ResponseEntity.ok(userService.updateProfile(username, request));
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change password for logged-in user")
    public ResponseEntity<Map<String, String>> changePassword(
            @RequestParam(defaultValue = "customer1") String username,
            @RequestBody ChangePasswordRequest request) {
        boolean success = userService.changePassword(username, request.getOldPassword(), request.getNewPassword());
        if (success) {
            return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Incorrect old password"));
    }
}