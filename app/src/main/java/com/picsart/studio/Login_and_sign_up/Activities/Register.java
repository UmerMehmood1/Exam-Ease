package com.picsart.studio.Login_and_sign_up.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.picsart.studio.DBHelper.FirebaseHelper;
import com.picsart.studio.Models.User;
import com.picsart.studio.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Register extends AppCompatActivity {

    EditText name, dob, password, c_password;
    Button register;
    RadioButton isStudent, isTeacher;
    FirebaseHelper firebaseHelperUser;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        calendar = Calendar.getInstance();
        firebaseHelperUser = new FirebaseHelper();
        isStudent = findViewById(R.id.isStudent);
        isTeacher = findViewById(R.id.isTeacher);

        name = findViewById(R.id.name);
        dob = findViewById(R.id.dob);
        dob.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showDatePicker();
            }
        });
        dob.setInputType(InputType.TYPE_NULL);
        password = findViewById(R.id.password);
        c_password = findViewById(R.id.c_password);
        register = findViewById(R.id.Register_btn);

        register.setOnClickListener(l -> {
            String username = name.getText().toString();
            String user_image = String.valueOf(R.drawable.img_1_intro);
            String user_badge = "Beginner";
            String user_dob = dob.getText().toString();
            String user_password = password.getText().toString();
            String user_confirm_password = c_password.getText().toString();
            String user_type = "Student";
            if (isStudent.isChecked()) {
                user_type = "Student";
            }
            else if (isTeacher.isChecked()) {
                user_type = "Instructor";
            }

            if (username == "") {
                Toast.makeText(this, "Username is empty", Toast.LENGTH_SHORT).show();
            }
            else if (username.length() < 8) {
                Toast.makeText(this, "Name must be of minimum 8 characters", Toast.LENGTH_SHORT).show();
            }
            else if (user_password.length() < 8) {
                Toast.makeText(this, "Password must be of minimum 8 characters", Toast.LENGTH_SHORT).show();
            }
            else if (user_password == "") {
                Toast.makeText(this, "Password is empty", Toast.LENGTH_SHORT).show();
            }
            else if (user_confirm_password == "") {
                Toast.makeText(this, "Confirm Password is empty", Toast.LENGTH_SHORT).show();
            }
            else if (!user_password.equals(user_confirm_password)) {
                Toast.makeText(this, "Both Passwords are not same.", Toast.LENGTH_SHORT).show();
            }
            else if (user_dob.isEmpty()) {
                Toast.makeText(this, "Date of birth is empty.", Toast.LENGTH_SHORT).show();
            }
            else if (!isStudent.isChecked() & !isTeacher.isChecked()){
                Toast.makeText(this, "Please select the type. i.e if you are Student select I am student.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "User is registering...", Toast.LENGTH_SHORT).show();
                firebaseHelperUser.addUser(new User(username, user_image, user_badge, user_dob, user_password, user_type))
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Registered Succesfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, login.class));
                            } else {
                                Toast.makeText(this, "Check internet connection", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            });




    }
    private void showDatePicker () {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDobEditText();
        };

        // Show the Date Picker with the current date as default
        new DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }
    private void updateDobEditText () {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(calendar.getTime());
        dob.setText(formattedDate);
    }
}
