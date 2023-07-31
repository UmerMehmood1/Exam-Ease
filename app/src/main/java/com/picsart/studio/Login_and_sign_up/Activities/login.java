package com.picsart.studio.Login_and_sign_up.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.picsart.studio.Instructor.Activities.Teacher_main;
import com.picsart.studio.Student.Activities.Student_main;
import com.picsart.studio.DBHelper.FirebaseHelper;
import com.picsart.studio.Models.User;
import com.picsart.studio.R;

public class login extends AppCompatActivity {

    EditText name, pass;
    Button btn_login;
    FirebaseHelper firebaseHelperUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = findViewById(R.id.name);
        pass = findViewById(R.id.password);

        btn_login = findViewById(R.id.Login_btn);

        btn_login.setOnClickListener(l -> {
            firebaseHelperUser = new FirebaseHelper();
            firebaseHelperUser.getUsersByNameAndPassword(name.getText().toString(), pass.getText().toString())
                    .addOnSuccessListener(documentSnapshots -> {
                        for (DocumentSnapshot document : documentSnapshots) {
                            User user = document.toObject(User.class);
                            if (user != null) {
                                User user_extracted = new User(document.getId(), user.getName(), user.getImg(), user.getBadge(), user.getDob(), user.getPassword(), user.getUser_type());
                                if ("Instructor".equals(user.getUser_type())) {
                                    SharedPreferences sharedPreferences = getSharedPreferences("teacher_data", MODE_PRIVATE);
                                    SharedPreferences.Editor teacher_editor = sharedPreferences.edit();
                                    teacher_editor.putString("id", document.getId());
                                    teacher_editor.putString("name", user_extracted.getName());
                                    teacher_editor.putString("img", user_extracted.getImg());
                                    teacher_editor.putString("badge", user_extracted.getBadge());
                                    teacher_editor.putString("dob", user.getDob());
                                    teacher_editor.apply();
                                    startActivity(new Intent(getApplicationContext(), Teacher_main.class));
                                } else {
                                    SharedPreferences sharedPreferences = getSharedPreferences("student_data", MODE_PRIVATE);
                                    SharedPreferences.Editor teacher_editor = sharedPreferences.edit();
                                    teacher_editor.putString("id", document.getId());
                                    teacher_editor.putString("name", user_extracted.getName());
                                    teacher_editor.putString("img", user_extracted.getImg());
                                    teacher_editor.putString("badge", user_extracted.getBadge());
                                    teacher_editor.putString("dob", user.getDob());
                                    teacher_editor.apply();
                                    startActivity(new Intent(getApplicationContext(), Student_main.class));
                                }
                            } else {
                                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle error if the query fails
                    });
        });
    }
}