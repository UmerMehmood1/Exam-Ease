package com.picsart.studio.Instructor.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.picsart.studio.DBHelper.FirebaseHelper;
import com.picsart.studio.Models.Course;
import com.picsart.studio.R;

public class Add_Course extends AppCompatActivity {

    EditText title, category, duration, t_quiz, description;
    Button AddCourse, Back;
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructor_course_add_activity);

        Intent intent = getIntent();
        String teacher_id = intent.getStringExtra("id");
        Toast.makeText(this, teacher_id, Toast.LENGTH_SHORT).show();
        title = findViewById(R.id.course_name_at_add_course);
        category = findViewById(R.id.course_category_at_add_course);
        duration = findViewById(R.id.course_estimated_duration_at_add_course);
        t_quiz = findViewById(R.id.course_total_quiz_at_add_course);
        description = findViewById(R.id.course_description_at_add_course);
        AddCourse = findViewById(R.id.AddCourseBtn);
        Back = findViewById(R.id.Back_btt_at_add_course);
        Back.setOnClickListener(l->{
            finish();
        });
        AddCourse.setOnClickListener(l->{
            firebaseHelper = new FirebaseHelper();
            firebaseHelper.addCourse(teacher_id, new Course(title.getText().toString(), category.getText().toString(), duration.getText().toString(),Integer.parseInt(t_quiz.getText().toString()), description.getText().toString(), R.drawable.available_courses,teacher_id))
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Course Added Succesfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}