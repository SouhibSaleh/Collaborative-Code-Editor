package com.example.lastproject.model.requests;

public class ChatMessage{
    private int id;
    private String username;
    private String message;
    private String date;

    public ChatMessage() {
        id = -1;
        username = "";
        message="";
        date="";
    }

    public ChatMessage(int id, String username, String message, String date) {
        this.id = id;
        this.username = username;
        this.message = message;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String usernmame) {
        this.username = usernmame;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "id=" + id +
                ", usernmame='" + username + '\'' +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
