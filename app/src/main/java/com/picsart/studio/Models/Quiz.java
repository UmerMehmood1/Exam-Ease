package com.picsart.studio.Models;

import com.google.firebase.database.PropertyName;
import com.picsart.studio.Models.Question;

import java.io.Serializable;
import java.util.List;

public class Quiz implements Serializable {
    @PropertyName("quiz_id")
    private String quizId;

    @PropertyName("quiz_name")
    private String quizName;

    @PropertyName("total_questions")
    private int totalQuestions;

    @PropertyName("course_id")
    private String courseId;

    @PropertyName("instructor_id")
    private String instructorId;

    private List<Question> questions;

    public Quiz() {
        // Empty constructor needed for Firestore deserialization
    }

    public Quiz(String quizId, String quizName, int totalQuestions, String courseId, String instructorId, List<Question> questions) {
        this.quizId = quizId;
        this.quizName = quizName;
        this.totalQuestions = totalQuestions;
        this.courseId = courseId;
        this.instructorId = instructorId;
        this.questions = questions;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
