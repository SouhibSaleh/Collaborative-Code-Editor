package com.example.lastproject.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @JoinColumn(name = "snapshot_id", referencedColumnName = "id", nullable = true)
    private Long snapshotId;
    @Column(nullable = false,unique = true)
    private String name;

    @Column(nullable = false)
    private String language;
    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;
    @PrePersist
    protected void onCreate() {
        this.creationDate = LocalDateTime.now();
    }
    public Project() {
    }

    public Project(Long id, Long userId, Long snapshotId, String name, String language, LocalDateTime creationDate) {
        this.id = id;
        this.userId = userId;
        this.snapshotId = snapshotId;
        this.name = name;
        this.language = language;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(Long snapshotId) {
        this.snapshotId = snapshotId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", userId=" + userId +
                ", snapshotId=" + snapshotId +
                ", name='" + name + '\'' +
                ", language='" + language + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}