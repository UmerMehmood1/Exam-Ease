package com.picsart.studio.Instructor.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

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
        Intent get_intent = getIntent();
        course_id = get_intent.getStringExtra("course_id");
        teacher_id = get_intent.getStringExtra("teacher_id");

        add_quiz.setOnClickListener(l->{
            SharedPreferences sharedPreferences = getSharedPreferences("quiz_data",MODE_PRIVATE);
            SharedPreferences.Editor teacher_editor = sharedPreferences.edit();
            teacher_editor.putString("quiz_name", quiz_name.getText().toString());
            teacher_editor.putLong("quiz_total_question", Long.parseLong(total_questions.getText().toString()));
            Intent intent = new Intent(AddQuiz.this, AddQuestionActivity.class);
            startActivity(intent);
            finish();
        });

    }

}