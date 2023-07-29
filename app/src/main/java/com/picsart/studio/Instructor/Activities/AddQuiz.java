package com.picsart.studio.Instructor.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.picsart.studio.DBHelper.FirebaseHelper;
import com.picsart.studio.Models.Question;
import com.picsart.studio.R;

import java.util.ArrayList;
import java.util.List;

public class AddQuiz extends AppCompatActivity {
    String course_id, teacher_id;
    private FirebaseHelper firebaseHelper;
    EditText quiz_name, total_questions;
    Button add_quiz;
    List<Question> questionsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructor_add_quiz_activity);
        firebaseHelper = new FirebaseHelper();
        questionsList = new ArrayList<>();
        quiz_name = findViewById(R.id.editTextQuizName);
        total_questions = findViewById(R.id.editTextTotalQuestions);
        add_quiz = findViewById(R.id.buttonAddQuiz);
        course_id = getIntent().getStringExtra("course_id");

        add_quiz.setOnClickListener(l->{
            Intent intent = new Intent(AddQuiz.this, AddQuestionActivity.class);
            intent.putExtra("course_id", course_id);
            intent.putExtra("quiz_name", quiz_name.getText().toString());
            intent.putExtra("quiz_total_question", Long.parseLong(total_questions.getText().toString()));
            startActivity(intent);
            finish();
        });

    }

}