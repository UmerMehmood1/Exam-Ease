package com.picsart.studio.Login_and_sign_up.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.picsart.studio.Instructor.Activities.Teacher_main;
import com.picsart.studio.SharedPreferenceHelper;
import com.picsart.studio.Student.Activities.Student_main;
import com.picsart.studio.DBHelper.FirebaseHelper;
import com.picsart.studio.Models.User;
import com.picsart.studio.R;

class Manual_Loggin_Inner{
    Context context;
    public Manual_Loggin_Inner(Context context) {
        this.context = context;
    }
    public void login_using_username_and_password(String username, String password) {
        FirebaseHelper firebaseHelperUser = new FirebaseHelper();
        firebaseHelperUser.getUsersByNameAndPassword(username, password)
                .addOnSuccessListener(documentSnapshots -> {
                    for (DocumentSnapshot document : documentSnapshots) {
                        User user = document.toObject(User.class);
                        user.setId("2");
                        if (user != null) {
                            Toast.makeText(context, "Logging in"+user.getUser_type(), Toast.LENGTH_SHORT).show();
                            if ("Instructor".equals(user.getUser_type())) {
                                SharedPreferenceHelper.INSTANCE.saveTeacherData(context, user, user.getId());
                                context.startActivity(new Intent(context, Teacher_main.class));
                            } else if ("teacher".equals(user.getUser_type()) || "Teacher".equals(user.getUser_type())){
                                SharedPreferenceHelper.INSTANCE.saveTeacherData(context, user, user.getId());
                                context.startActivity(new Intent(context, Teacher_main.class));
                            }
                            else if ("Student".equals(user.getUser_type()) || "student".equals(user.getUser_type())) {
                                SharedPreferenceHelper.INSTANCE.saveStudentData(context, user, user.getId());
                                context.startActivity(new Intent(context, Student_main.class));
                            }
                            else{
                                SharedPreferenceHelper.INSTANCE.saveStudentData(context, user, user.getId());
                                context.startActivity(new Intent(context, Student_main.class));
                            }
                        } else {
                            Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Check internet connection", Toast.LENGTH_SHORT).show();
                });
    }
}
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
            Manual_Loggin_Inner login = new Manual_Loggin_Inner(this);
            login.login_using_username_and_password(name.getText().toString(),pass.getText().toString());
        });
    }
}


