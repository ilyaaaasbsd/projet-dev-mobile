package com.example.caftanrental;
public class LoginResponse {
    private String message;
    private String role;     // "admin" or "user"
    private String username;

    public String getRole() { return role; }
    public String getMessage() { return message; }
}