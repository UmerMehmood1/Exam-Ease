package com.picsart.studio.Instructor.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.picsart.studio.DBHelper.FirebaseHelper;
import com.picsart.studio.Models.Course;
import com.picsart.studio.R;

public class UpdateCourseActivity extends AppCompatActivity {
    EditText title, category, duration, t_quiz, description;
    String Courseid, teacher_id;
    Button Update_Course, Back;
    FirebaseHelper firebaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructor_course_update_activity);
        title = findViewById(R.id.course_name_at_update_course);
        category = findViewById(R.id.course_category_at_update_course);
        duration = findViewById(R.id.course_estimated_duration_at_update_course);
        t_quiz = findViewById(R.id.course_total_quiz_at_update_course);
        description = findViewById(R.id.course_description_at_update_course);
        Update_Course = findViewById(R.id.UpdateCourseBtn);
        Back = findViewById(R.id.Back_btt_at_update_course);

        Back.setOnClickListener(l->{
            finish();
        });
        Courseid = getIntent().getStringExtra("Course_id");
        teacher_id = getIntent().getStringExtra("teacher_id");
        Update_Course.setOnClickListener(l->{
            firebaseHelper = new FirebaseHelper();
            firebaseHelper.updateCourse(Courseid, new Course(title.getText().toString(), category.getText().toString(), duration.getText().toString(),Integer.parseInt(t_quiz.getText().toString()), description.getText().toString(), R.drawable.available_courses,teacher_id))
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(UpdateCourseActivity.this, "Course Added Succesfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(UpdateCourseActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
        });



    }
}