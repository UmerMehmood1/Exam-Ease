package com.picsart.studio.Student.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.picsart.studio.DBHelper.FirebaseHelper;
import com.picsart.studio.R;

public class Course_Detail extends AppCompatActivity {
    TextView name, category, course_description;
    ImageView course_img;
    FirebaseHelper firebaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_course_details);
        Intent data = getIntent();
        String id = data.getStringExtra("Student_id");
        String course_id = data.getStringExtra("Course_id");
        String course_title = data.getStringExtra("Title");
        int img = data.getIntExtra("Thumbnail",R.drawable.achievement_svgrepo_com);
        String description = data.getStringExtra("Description");
        String category1 = data.getStringExtra("Category");

        name = findViewById(R.id.course_title_at_details);
        name.setText(course_title);
        category = findViewById(R.id.course_category_at_details);
        category.setText(category1);
        course_description = findViewById(R.id.course_description_at_details);
        course_description.setText(description);
        course_img = findViewById(R.id.course_img_at_details);
        course_img.setImageResource(img);

        findViewById(R.id.back_btn).setOnClickListener(l->{finish();});
        findViewById(R.id.enroll_btn).setOnClickListener(l->{
            firebaseHelper = new FirebaseHelper();
            firebaseHelper.enrollStudentInCourse(id,course_id)
                    .addOnCompleteListener(result -> {
                        if (result.isSuccessful()) {
                            Toast.makeText(this, "Enrolled SuccessfullyEnrolled Successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(listener -> {
                            Toast.makeText(this, "Check internet connection", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
