package com.example.lastproject.model.requests;

public class LockLineRequest {
    private int userId;
    private int lineNumber;

    public LockLineRequest() {
    }

    public LockLineRequest(int userId, int lineNumber) {
        this.userId = userId;
        this.lineNumber = lineNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    @Override
    public String toString() {
        return "LockLineRequest{" +
                "userId=" + userId +
                ", lineNumber=" + lineNumber +
                '}';
    }
}
