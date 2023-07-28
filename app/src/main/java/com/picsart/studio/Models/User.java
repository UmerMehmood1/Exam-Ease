package com.picsart.studio.Models;

public class User {

    private String id; // Change the data type to String
    private String name;
    private String img;
    private String badge;
    private String dob;
    private String password;
    private String user_type;

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    // Constructors (you can have multiple constructors based on your needs)
    public User() {
    }

    public User(String id, String name, String img, String badge, String dob, String password, String user_type) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.badge = badge;
        this.dob = dob;
        this.password = password;
        this.user_type = user_type;
    }

    public User(String name, String img, String badge, String dob, String password, String user_type) {
        this.name = name;
        this.img = img;
        this.badge = badge;
        this.dob = dob;
        this.password = password;
        this.user_type = user_type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
