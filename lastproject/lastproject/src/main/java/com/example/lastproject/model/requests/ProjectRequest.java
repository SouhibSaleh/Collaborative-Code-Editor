package com.example.lastproject.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public  class ProjectRequest{
    @JsonProperty
    private String name;
    @JsonProperty
    private String language;

    public ProjectRequest() {
    }

    public ProjectRequest(String name, String language) {
        this.name = name;
        this.language = language;
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

    @Override
    public String toString() {
        return "ProjectRequest{" +
                "name='" + name + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}