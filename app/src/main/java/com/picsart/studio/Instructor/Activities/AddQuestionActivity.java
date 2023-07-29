package com.picsart.studio.Instructor.Activities;

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
        this.quiz_name = getIntent().getStringExtra("quiz_name");
        this.total_question = (int) getIntent().getLongExtra("quiz_total_question", 1);
        this.course_id = getIntent().getStringExtra("course_id");
        this.teacher_id = getSharedPreferences("teacher_data", MODE_PRIVATE).getString("id","");
        Toast.makeText(this, course_id + " at Quiz Clicked.", Toast.LENGTH_SHORT).show();

        questionList = new ArrayList<>();
        List<String> data = Arrays.asList(new String[]{"Option 1", "Option 1","Option 1","Option 1"});
        for (int i = 0; i < total_question; i++){
            questionList.add(new Question("Some Content",data, 1));
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