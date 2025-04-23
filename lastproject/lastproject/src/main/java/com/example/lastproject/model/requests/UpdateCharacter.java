package com.example.lastproject.model.requests;

public  class UpdateCharacter {

    private long userId;
    private int index;
    private String value;

    public UpdateCharacter() {
        index = 0;
        value = "";
    }
    public UpdateCharacter(long userId, int index, String value) {
        this.userId = userId;
        this.index = index;
        this.value = value;
    }

    @Override
    public String toString() {
        return "UpdateCharacter{" +
                "userId=" + userId +
                ", index=" + index +
                ", value='" + value + '\'' +
                '}';
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
