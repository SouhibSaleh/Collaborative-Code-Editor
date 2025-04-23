package com.example.lastproject.model.responses;


public class AuthResponse {
    private long id;
    private String email;
    private String token;
    private String role;
    private String message;

    public AuthResponse() {
        this.id = -1;
        this.token = null;
        this.email =null;
        this.role = null;
        this.message = null;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                ", role='" + role + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public AuthResponse(long id, String email, String token, String role, String message) {
        this.id = id;
        this.email = email;
        this.token = token;
        this.role = role;
        this.message = message;
    }
}
