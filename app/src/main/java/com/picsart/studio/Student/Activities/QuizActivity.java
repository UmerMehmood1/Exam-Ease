package com.picsart.studio.Student.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.picsart.studio.Models.Question;
import com.picsart.studio.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView textViewQuestion;
    private RadioGroup radioGroupOptions;
    private Button buttonSubmit;

    private List<Question> quizQuestions;
    private int currentQuestionIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_quiz_helding);

        // Initialize views
        textViewQuestion = findViewById(R.id.textViewQuestion);
        radioGroupOptions = findViewById(R.id.radioGroupOptions);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Sample quiz questions - replace with your actual quiz questions
        quizQuestions = getSampleQuizQuestions();

        // Initialize current question index
        currentQuestionIndex = 0;

        // Display the first question
        displayQuestion();

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the answer submission for the current question
                handleAnswerSubmission();
            }
        });
    }

    // Sample method to get quiz questions - Replace this with your actual method to retrieve quiz questions
    private List<Question> getSampleQuizQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("Question 1: What is 2 + 2?", Arrays.asList("3", "4", "5", "6"), 1));
        questions.add(new Question("Question 2: What is the capital of France?", Arrays.asList("London", "Paris", "Berlin", "Rome"), 1));
        // Add more quiz questions as needed
        return questions;
    }

    private void displayQuestion() {
        if (currentQuestionIndex < quizQuestions.size()) {
            Question currentQuestion = quizQuestions.get(currentQuestionIndex);
            textViewQuestion.setText(currentQuestion.getQuestionText());

            // Clear previously selected answer
            radioGroupOptions.clearCheck();

            // Add answer options dynamically
            radioGroupOptions.removeAllViews();
            for (int i = 0; i < currentQuestion.getOptions().size(); i++) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(currentQuestion.getOptions().get(i));
                radioGroupOptions.addView(radioButton);
            }
        } else {
            // Quiz completed, show a message or navigate to the next activity, etc.
            // For example, you can show a Toast message:
            showToast("Quiz completed!");
        }
    }

    private void handleAnswerSubmission() {
        if (currentQuestionIndex < quizQuestions.size()) {
            // Get the selected answer index from RadioGroup
            int selectedOptionIndex = radioGroupOptions.indexOfChild(findViewById(radioGroupOptions.getCheckedRadioButtonId()));

            // Compare the selected option with the correct option index for the current question
            Question currentQuestion = quizQuestions.get(currentQuestionIndex);
            if (selectedOptionIndex == currentQuestion.getCorrectOptionIndex()) {
                showToast("Correct!");
            } else {
                showToast("Incorrect!");
            }

            // Move to the next question
            currentQuestionIndex++;
            displayQuestion();
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
