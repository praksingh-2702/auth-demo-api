package com.example.auth_demo.controller;

import com.example.auth_demo.dto.*;
import com.example.auth_demo.model.CustomerStatus;
import com.example.auth_demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/customer")
@CrossOrigin(origins = "*")
@Tag(name = "Customer Management", description = "Endpoints for managing customer profiles and status")
public class CustomerController {

    private final UserService userService;

    public CustomerController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    @Operation(summary = "Get current logged-in customer profile")
    public ResponseEntity<UserResponse> getProfile(@RequestParam(defaultValue = "customer1") String username) {
        return ResponseEntity.ok(userService.getUserProfile(username));
    }

    @PostMapping("/profile")
    @Operation(summary = "Customer onboarding / Submit additional profile KYC info")
    public ResponseEntity<UserResponse> completeOnboarding(
            @RequestParam(defaultValue = "customer1") String username,
            @RequestBody CustomerKycRequest request) {
        return ResponseEntity.ok(userService.updateProfile(username, request));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all customer profiles")
    public ResponseEntity<List<UserResponse>> getAllProfiles() {
        return ResponseEntity.ok(userService.getAllProfiles());
    }

    @GetMapping("/status")
    @Operation(summary = "Retrieve current customer status")
    public ResponseEntity<Map<String, CustomerStatus>> getStatus(@RequestParam(defaultValue = "customer1") String username) {
        return ResponseEntity.ok(Map.of("status", userService.getStatus(username)));
    }
}