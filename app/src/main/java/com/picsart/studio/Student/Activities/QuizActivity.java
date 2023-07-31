package com.picsart.studio.Student.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.picsart.studio.DBHelper.FirebaseHelper;
import com.picsart.studio.Models.Question;
import com.picsart.studio.Models.Quiz;
import com.picsart.studio.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView textViewQuestion;
    private RadioGroup radioGroupOptions;
    private Button buttonSubmit;
    private FirebaseHelper firebaseHelper;
    private List<Question> quizQuestions;
    private int currentQuestionIndex;
    private int scores = 0;
    String courseId, quizId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_quiz_helding);
        Intent intent = getIntent();
        Quiz selectedQuiz = (Quiz) intent.getSerializableExtra("selectedQuiz");
        courseId = intent.getStringExtra("course_id");
        quizId = intent.getStringExtra("quiz_id");

        // Initialize views
        textViewQuestion = findViewById(R.id.textViewQuestion);
        radioGroupOptions = findViewById(R.id.radioGroupOptions);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Sample quiz questions - replace with your actual quiz questions
        quizQuestions = selectedQuiz.getQuestions();

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
        }
        else {
            firebaseHelper = new FirebaseHelper();
            SharedPreferences sharedPreferences = getSharedPreferences("student_data", MODE_PRIVATE);
            String userId = sharedPreferences.getString("id","");
            firebaseHelper.storeQuizAttempt(userId,courseId,quizId,scores);
            finish();
            showToast("Quiz completed!");
        }
    }

    private void handleAnswerSubmission() {
        if (currentQuestionIndex < quizQuestions.size()) {
            int selectedOptionIndex = radioGroupOptions.indexOfChild(findViewById(radioGroupOptions.getCheckedRadioButtonId()));
            Question currentQuestion = quizQuestions.get(currentQuestionIndex);
            if (selectedOptionIndex == currentQuestion.getCorrectOptionIndex()) {
                showToast("Correct!");
                scores++;
            } else {
                showToast("Incorrect!");
            }
            currentQuestionIndex++;
            displayQuestion();
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
