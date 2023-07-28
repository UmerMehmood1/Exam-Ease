package com.picsart.studio.Instructor.InstructorFragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.picsart.studio.Instructor.Adapter.QuizAdapterAtLeadingCourse;
import com.picsart.studio.Instructor.Activities.AddQuiz;
import com.picsart.studio.Models.Quiz;
import com.picsart.studio.R;

import java.util.ArrayList;
import java.util.List;

public class CourseQuizzes extends AppCompatActivity {

    String name, category, description, duration, course_id, teacher_id;
    int course_img;
    TextView course_title,course_category, course_description;
    RecyclerView recyclerView;
    List<Quiz> quizList;
    ImageButton back;
    FloatingActionButton floating_button_add_quiz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructor_quiz_activity);
        quizList = new ArrayList<>();
        SharedPreferences sh = getSharedPreferences("course_data", Context.MODE_PRIVATE);
        this.course_id = sh.getString("course_id","");
        this.name = sh.getString("course_name","");
        this.course_img = sh.getInt("course_img",0);
        this.category = sh.getString("course_category","");
        this.description = sh.getString("course_description","");
        this.duration = sh.getString("course_duration","");
        course_title = findViewById(R.id.coursse_title_at_instructor_quiz_view);
        course_category = findViewById(R.id.course_category_at_instructor_quiz_view);
        course_description = findViewById(R.id.course_description_at_instructor_quiz_view);
        recyclerView = findViewById(R.id.recycler_view_for_quiz_in_course_enrolled);
        floating_button_add_quiz = findViewById(R.id.add_quiz_floating_button);
        back = findViewById(R.id.back_btn);
        back.setOnClickListener(l->{finish();});

        course_title.setText(name);
        course_category.setText(category);
        course_description.setText(description);

        QuizAdapterAtLeadingCourse quizAdapterAtLeadingCourse = new QuizAdapterAtLeadingCourse(this, quizList, teacher_id, course_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(quizAdapterAtLeadingCourse);
        floating_button_add_quiz.setOnClickListener(l->{
            Intent intent1 = new Intent(this, AddQuiz.class);
            intent1.putExtra("teacher_id",teacher_id);
            intent1.putExtra("course_id",teacher_id);
            startActivity(intent1);
        });

    }
}