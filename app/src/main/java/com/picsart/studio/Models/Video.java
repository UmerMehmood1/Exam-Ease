package com.picsart.studio.Models;

public class Video {
    private String title;
    private String description;
    private String url;
    private String course_id;

    public Video() {
        // Needed for Firebase deserialization
    }

    public Video(String title, String description, String url, String course_id) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.course_id = course_id;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getCourse_id() {
        return course_id;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }
}
