package com.example.lastproject.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public  class SaveSnapshotRequest{
    @JsonProperty
    private int projectId;
    @JsonProperty
    private String code;

    @JsonProperty
    private String comments;

    public SaveSnapshotRequest() {
    }

    @Override
    public String toString() {
        return "SaveSnapshotRequest{" +
                "projectId=" + projectId +
                ", code='" + code + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SaveSnapshotRequest(int projectId, String code) {
        this.projectId = projectId;
        this.code = code;
    }
}