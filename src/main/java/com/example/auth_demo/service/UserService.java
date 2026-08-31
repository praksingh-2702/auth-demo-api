package com.example.auth_demo.service;

import com.example.auth_demo.dto.*;
import com.example.auth_demo.model.CustomerStatus;

import java.util.List;

public interface UserService {
    UserResponse register(RegisterRequest request);
    String login(LoginRequest request);
    boolean verifyOtp(OtpVerificationRequest request);
    String refreshToken(String token);
    String initiatePasswordReset(String email);
    boolean resetPassword(String token, String newPassword);
    boolean changePassword(String username, String oldPassword, String newPassword);
    UserResponse getUserProfile(String username);
    UserResponse updateProfile(String username, CustomerKycRequest request);
    List<UserResponse> getAllProfiles();
    CustomerStatus getStatus(String username);
}