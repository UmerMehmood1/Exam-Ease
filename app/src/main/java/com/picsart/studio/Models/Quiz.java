package com.picsart.studio.Models;

import java.util.List;

public class Quiz {
    private String quiz_id;
    private String quiz_name;
    private int total_questions;
    private String course_id;
    private String instructor_id;
    private List<Question> questions;

    public Quiz() {
        // Empty constructor needed for Firestore deserialization
    }

    public Quiz(String quiz_id, String quiz_name, int total_questions, String course_id, String instructor_id, List<Question> questions) {
        this.quiz_id = quiz_id;
        this.quiz_name = quiz_name;
        this.total_questions = total_questions;
        this.course_id = course_id;
        this.instructor_id = instructor_id;
        this.questions = questions;
    }

    public String getQuizId() {
        return quiz_id;
    }

    public void setQuizId(String quiz_id) {
        this.quiz_id = quiz_id;
    }

    public String getQuizName() {
        return quiz_name;
    }

    public void setQuizName(String quiz_name) {
        this.quiz_name = quiz_name;
    }

    public int getTotalQuestions() {
        return total_questions;
    }

    public void setTotalQuestions(int total_questions) {
        this.total_questions = total_questions;
    }

    public String getCourseId() {
        return course_id;
    }

    public void setCourseId(String course_id) {
        this.course_id = course_id;
    }

    public String getInstructorId() {
        return instructor_id;
    }

    public void setInstructorId(String instructor_id) {
        this.instructor_id = instructor_id;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}

