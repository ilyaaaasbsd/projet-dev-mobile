package com.example.caftanrental;
public class LoginRequest {
    private String username;
    private String password;
    private String role; // Used for signup only

    public LoginRequest(String username, String password) { // For Login
        this.username = username;
        this.password = password;
    }

    public LoginRequest(String username, String password, String role) { // For Signup
        this.username = username;
        this.password = password;
        this.role = role;
    }
}