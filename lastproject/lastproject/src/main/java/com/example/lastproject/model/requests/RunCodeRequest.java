package com.example.lastproject.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RunCodeRequest {
    @JsonProperty
    private long snapshotId;
    @JsonProperty
    private String code;
    @JsonProperty
    private String args;
    @JsonProperty
    private String language;
    public RunCodeRequest() {
    }

    public RunCodeRequest(long snapshotId, String code, String args, String language) {
        this.snapshotId = snapshotId;
        this.code = code;
        this.args = args;
        this.language = language;
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

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "RunCodeRequest{" +
                "snapshotId=" + snapshotId +
                ", code='" + code + '\'' +
                ", args='" + args + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
