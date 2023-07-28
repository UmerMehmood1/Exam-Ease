package com.picsart.studio.Instructor.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.picsart.studio.Instructor.Adapter.QuestionAdapter;
import com.picsart.studio.DBHelper.FirebaseHelper;
import com.picsart.studio.Models.Question;
import com.picsart.studio.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddQuestionActivity extends AppCompatActivity {

    List<Question> questionList;
    RecyclerView recyclerView;
    Button add_to_quiz;
    String teacher_id, course_id, quiz_name;
    FirebaseHelper firebaseHelper;
    int total_question;
    List<String> options;
    QuestionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        options = new ArrayList<>();
        setContentView(R.layout.instructor_add_question_activity);
        SharedPreferences quiz_data = getSharedPreferences("quiz_data",MODE_PRIVATE);
        this.quiz_name = quiz_data.getString("quiz_name", "");
        this.total_question = (int) quiz_data.getLong("quiz_total_question", 1);
        SharedPreferences course_data = getSharedPreferences("course_data",MODE_PRIVATE);
        this.course_id = course_data.getString("course_id","");
        SharedPreferences teacher_data = getSharedPreferences("teacher_data",MODE_PRIVATE);
        this.teacher_id = teacher_data.getString("teacher_id","");

        questionList = new ArrayList<>();
        List<String> data = Arrays.asList(new String[]{"Option 1", "Option 1","Option 1","Option 1"});
        for (int i = 0; i < total_question; i++){
            questionList.add(new Question("Some Content",data, 1));
            Toast.makeText(this, questionList.toString(), Toast.LENGTH_SHORT).show();
        }
        recyclerView = findViewById(R.id.recycler_view_to_add_questions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new QuestionAdapter(this, teacher_id, course_id, questionList);
        recyclerView.setAdapter(adapter);

        add_to_quiz = findViewById(R.id.Add_Questions_btn);
        add_to_quiz.setOnClickListener(v -> {
            questionList = adapter.getQuestionList();
            firebaseHelper = new FirebaseHelper();
            firebaseHelper.addQuiz(quiz_name, total_question, course_id, teacher_id, questionList);
            finish();
        });
    }
}