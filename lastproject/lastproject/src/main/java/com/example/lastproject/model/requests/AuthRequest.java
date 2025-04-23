package com.example.lastproject.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

 public class AuthRequest{
    @JsonProperty
    private String email;
    @JsonProperty
    private String password;
    @JsonProperty
    private boolean isAdmin;

    public AuthRequest(String email, String password, boolean isAdmin) {
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }
    public AuthRequest(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        return "AuthRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}