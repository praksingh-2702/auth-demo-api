package com.example.auth_demo.service;

import com.example.auth_demo.dto.*;
import com.example.auth_demo.model.CustomerStatus;
import com.example.auth_demo.model.Role;
import com.example.auth_demo.model.UserEntity;
import com.example.auth_demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse register(RegisterRequest request) {
        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setRole(Role.CUSTOMER);
        user.setStatus(CustomerStatus.PENDING_VERIFICATION);
        user.setOtp("123456");

        UserEntity saved = userRepository.save(user);
        return mapToUserResponse(saved);
    }

    @Override
    public String login(LoginRequest request) {
        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return "mock-jwt-token-" + user.getUsername();
    }

    @Override
    public boolean verifyOtp(OtpVerificationRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .map(user -> {
                    if ("123456".equals(request.getOtp()) || request.getOtp().equals(user.getOtp())) {
                        user.setStatus(CustomerStatus.ACTIVE);
                        userRepository.save(user);
                        return true;
                    }
                    return false;
                }).orElse(false);
    }

    @Override
    public String refreshToken(String token) {
        return "mock-refreshed-jwt-token-" + UUID.randomUUID();
    }

    @Override
    public String initiatePasswordReset(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        userRepository.save(user);
        return resetToken;
    }

    @Override
    public boolean resetPassword(String token, String newPassword) {
        return userRepository.findByResetToken(token)
                .map(user -> {
                    user.setPassword(newPassword);
                    user.setResetToken(null);
                    userRepository.save(user);
                    return true;
                }).orElse(false);
    }

    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        return userRepository.findByUsername(username)
                .map(user -> {
                    if (user.getPassword().equals(oldPassword)) {
                        user.setPassword(newPassword);
                        userRepository.save(user);
                        return true;
                    }
                    return false;
                }).orElse(false);
    }

    @Override
    public UserResponse getUserProfile(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseGet(() -> createFallbackUser(username));
        return mapToUserResponse(user);
    }

    @Override
    public UserResponse updateProfile(String username, CustomerKycRequest request) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseGet(() -> createFallbackUser(username));

        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        if (request.getAddress() != null) user.setAddress(request.getAddress());
        if (request.getNationalId() != null) user.setNationalId(request.getNationalId());

        UserEntity updated = userRepository.save(user);
        return mapToUserResponse(updated);
    }

    @Override
    public List<UserResponse> getAllProfiles() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerStatus getStatus(String username) {
        return userRepository.findByUsername(username)
                .map(UserEntity::getStatus)
                .orElse(CustomerStatus.PENDING_VERIFICATION);
    }

    private UserEntity createFallbackUser(String username) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(username + "@example.com");
        user.setPassword("password");
        user.setRole(Role.CUSTOMER);
        user.setStatus(CustomerStatus.ACTIVE);
        return userRepository.save(user);
    }

    private UserResponse mapToUserResponse(UserEntity user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getNationalId(),
                user.getStatus(),
                user.getRole()
        );
    }
}