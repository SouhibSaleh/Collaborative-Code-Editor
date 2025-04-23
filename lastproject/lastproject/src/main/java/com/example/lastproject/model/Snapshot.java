package com.example.lastproject.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "snapshot")
public class Snapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(columnDefinition = "TEXT" ,nullable = true)
    private String code;

    @Column(name = "project_id")
    private long projectId;
    @Column(name="user_id")
    private long userId;
    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;
    @Column(name = "comments",columnDefinition = "TEXT")
    private String comments;
    @PrePersist
    protected void onCreate() {
        this.creationDate = LocalDateTime.now();
    }

    public Snapshot() {
        comments="[]";
    }

    public Snapshot(long id, String code, long projectId, long userId, LocalDateTime creationDate, String comments) {
        this.id = id;
        this.code = code;
        this.projectId = projectId;
        this.userId = userId;
        this.creationDate = creationDate;
        this.comments = comments;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Snapshot{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", projectId=" + projectId +
                ", userId=" + userId +
                ", creationDate=" + creationDate +
                ", comments='" + comments + '\'' +
                '}';
    }
}
