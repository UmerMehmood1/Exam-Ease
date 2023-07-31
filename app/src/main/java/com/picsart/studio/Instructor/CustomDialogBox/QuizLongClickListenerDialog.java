package com.picsart.studio.Instructor.CustomDialogBox;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.picsart.studio.DBHelper.FirebaseHelper;
import com.picsart.studio.Models.Course;
import com.picsart.studio.R;
import com.picsart.studio.Instructor.Activities.UpdateQuiz;
public class QuizLongClickListenerDialog extends Dialog {
    Button Update_Quiz, Delete_Quiz;
    FirebaseHelper firebaseHelper;
    public QuizLongClickListenerDialog(@NonNull Context context, String quiz_id, String course_id) {
        super(context);
        setContentView(R.layout.instructor_crud_dialog);
        Update_Quiz = findViewById(R.id.update_btn_at_quiz);
        Delete_Quiz = findViewById(R.id.delete_btn_at_quiz);
        firebaseHelper = new FirebaseHelper();
        Delete_Quiz.setOnClickListener(l->{
            firebaseHelper.deleteQuiz(quiz_id).addOnCompleteListener(q->{
                Toast.makeText(context, "Quiz is Deleted", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(t->{
                Toast.makeText(context, "There was problem while deleteing quiz", Toast.LENGTH_SHORT).show();
            });
        });
        Update_Quiz.setOnClickListener(l->{
            Intent intent = new Intent(context, UpdateQuiz.class);
            intent.putExtra("quiz_id", quiz_id);
            intent.putExtra("course_id", course_id);
            context.startActivity(intent);
        });
    }
}

