package com.example.auth_demo.dto;

import com.example.auth_demo.model.CustomerStatus;
import com.example.auth_demo.model.Role;

public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String nationalId;
    private CustomerStatus status;
    private Role role;

    public UserResponse(Long id, String username, String email, String fullName, String phoneNumber, String address, String nationalId, CustomerStatus status, Role role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.nationalId = nationalId;
        this.status = status;
        this.role = role;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getAddress() { return address; }
    public String getNationalId() { return nationalId; }
    public CustomerStatus getStatus() { return status; }
    public Role getRole() { return role; }
}