package com.picsart.studio.Models;

public class Course {

    private String id;
    private String Teacher_id;
    private String name;
    private String category;
    private String duration;
    private int totalQuizzes;
    private String description;
    private int img;

    // Constructors (you can have multiple constructors based on your needs)
    public Course() {
    }

    public Course(String id, String teacher_id, String name, String category, String duration, int totalQuizzes, String description, int img) {
        this.id = id;
        Teacher_id = teacher_id;
        this.name = name;
        this.category = category;
        this.duration = duration;
        this.totalQuizzes = totalQuizzes;
        this.description = description;
        this.img = img;
    }

    public String getTeacher_id() {
        return Teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        Teacher_id = teacher_id;
    }

    public Course(String name, String category, String duration, int totalQuizzes, String description, int img, String Teacher_id) {
        this.name = name;
        this.category = category;
        this.duration = duration;
        this.totalQuizzes = totalQuizzes;
        this.description = description;
        this.img = img;
        this.Teacher_id = Teacher_id;
    }

    // Getter and Setter methods for all attributes
    // (You can generate these automatically in Android Studio using Alt + Insert)

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTotalQuizzes() {
        return String.valueOf(totalQuizzes);
    }

    public void setTotalQuizzes(int totalQuizzes) {
        this.totalQuizzes = totalQuizzes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
