package com.picsart.studio.Login_and_sign_up.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.picsart.studio.DBHelper.FirebaseHelper;
import com.picsart.studio.Models.User;
import com.picsart.studio.R;

public class Register extends AppCompatActivity {

    EditText name, dob, password, c_password;
    Button register;
    FirebaseHelper firebaseHelperUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        dob = findViewById(R.id.dob);
        password = findViewById(R.id.password);
        c_password = findViewById(R.id.c_password);
        register = findViewById(R.id.Register_btn);

        register.setOnClickListener(l->{
            firebaseHelperUser = new FirebaseHelper();
            firebaseHelperUser.addUser(new User(name.getText().toString(),String.valueOf(R.drawable.img_1_intro),"Beginner",dob.getText().toString(), password.getText().toString(),"Student"))
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Registered Succesfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, login.class));
                        } else {
                            Toast.makeText(this, "Check internet connection", Toast.LENGTH_SHORT).show();
                        }
                    Toast.makeText(this, "Firebase Add user was called", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}