package com.picsart.studio.Models;
import java.io.Serializable;
import java.util.List;
public class Question implements Serializable {
    private String question_text;
    private List<String> options;
    private int correctOptionIndex;
    public Question() {}
    public Question(String question_text, List<String> options, int correctOptionIndex) {
        this.question_text = question_text;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }
    public String getQuestionText() {
        return question_text;
    }
    public void setQuestionText(String questionText) {
        this.question_text = questionText;
    }
    public List<String> getOptions() {
        return options;
    }
    public void setOptions(List<String> options) {
        this.options = options;
    }
    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }
    public void setCorrectOptionIndex(int correctOptionIndex) {
        this.correctOptionIndex = correctOptionIndex;
    }
}