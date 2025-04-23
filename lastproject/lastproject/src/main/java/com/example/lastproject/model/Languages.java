package com.example.lastproject.model;

import jakarta.persistence.*;

@Entity
@Table(name = "languages")
public class Languages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "language")
    private String language;
    public Languages() {
    }
    public Languages(long id, String language) {
        this.id = id;
        this.language = language;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "Languages{" +
                "id=" + id +
                ", language='" + language + '\'' +
                '}';
    }
}
