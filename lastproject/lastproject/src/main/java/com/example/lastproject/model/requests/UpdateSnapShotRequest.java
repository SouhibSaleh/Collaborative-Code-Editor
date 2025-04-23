package com.example.lastproject.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateSnapShotRequest{
    @JsonProperty
    private long snapshotId;
    @JsonProperty
    private String code;

    public UpdateSnapShotRequest() {
    }

    public UpdateSnapShotRequest(long snapshotId, String code) {
        this.snapshotId = snapshotId;
        this.code = code;
    }

    public long getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(long snapshotId) {
        this.snapshotId = snapshotId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "UpdateSnapShotRequest{" +
                "snapshotId=" + snapshotId +
                ", code='" + code + '\'' +
                '}';
    }
}