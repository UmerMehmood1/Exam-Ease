package com.picsart.studio.Models;

public class Quiz_Attempts {
    String course_id, quiz_id, score, user_id;

    public Quiz_Attempts(String course_id, String quiz_id, String score, String user_id) {
        this.course_id = course_id;
        this.quiz_id = quiz_id;
        this.score = score;
        this.user_id = user_id;
    }

    public Quiz_Attempts() {
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(String quiz_id) {
        this.quiz_id = quiz_id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
