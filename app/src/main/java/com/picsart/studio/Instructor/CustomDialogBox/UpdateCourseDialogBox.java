package com.picsart.studio.Instructor.CustomDialogBox;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.picsart.studio.DBHelper.FirebaseHelper;
import com.picsart.studio.Models.Course;
import com.picsart.studio.R;

public class UpdateCourseDialogBox extends Dialog {
    EditText title, category, duration, t_quiz, description;
    Button Update_Course, Back;
    FirebaseHelper firebaseHelper;

    public UpdateCourseDialogBox(@NonNull Context context, String Courseid, String teacher_id) {
        super(context);
        setContentView(R.layout.instructor_course_update_dialog_box);
        title = findViewById(R.id.course_name_at_update_course);
        category = findViewById(R.id.course_category_at_update_course);
        duration = findViewById(R.id.course_estimated_duration_at_update_course);
        t_quiz = findViewById(R.id.course_total_quiz_at_update_course);
        description = findViewById(R.id.course_description_at_update_course);
        Update_Course = findViewById(R.id.UpdateCourseBtn);
        Back = findViewById(R.id.Back_btt_at_update_course);

        Back.setOnClickListener(l->{
            dismiss();
        });
        Update_Course.setOnClickListener(l->{
            firebaseHelper = new FirebaseHelper();
            firebaseHelper.updateCourse(teacher_id, Courseid, new Course(title.getText().toString(), category.getText().toString(), duration.getText().toString(),Integer.parseInt(t_quiz.getText().toString()), description.getText().toString(), R.drawable.available_courses,teacher_id))
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Course Added Succesfully", Toast.LENGTH_SHORT).show();
                            dismiss();
                        } else {
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
        });



    }
}
