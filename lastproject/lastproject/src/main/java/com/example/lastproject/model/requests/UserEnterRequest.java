package com.example.lastproject.model.requests;

public class UserEnterRequest {
    private String username;
    private boolean enter;

    public UserEnterRequest() {
    }

    public UserEnterRequest(String username, boolean enter) {
        this.username = username;
        this.enter = enter;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isEnter() {
        return enter;
    }

    public void setEnter(boolean enter) {
        this.enter = enter;
    }

    @Override
    public String toString() {
        return "UserEnterRequest{" +
                "username='" + username + '\'' +
                ", enter=" + enter +
                '}';
    }
}
