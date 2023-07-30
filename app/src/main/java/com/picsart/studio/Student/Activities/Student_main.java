package com.picsart.studio.Student.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.picsart.studio.Student.StudentFragments.course_enrolled;
import com.picsart.studio.Student.StudentFragments.course_available;
import com.picsart.studio.Student.StudentFragments.student_profile;
import com.picsart.studio.Student.StudentFragments.student_search;
import com.picsart.studio.R;

public class Student_main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_interface);
        SharedPreferences sh = getSharedPreferences("student_data",MODE_PRIVATE);
        String id = sh.getString("id","");
        String name = sh.getString("name","");
        String image = sh.getString("img", "");
        String badge = sh.getString("badge","");
        String dob = sh.getString("dob","");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.student_fragement_viewer, new course_enrolled())
                    .commit();
        }

        BottomNavigationView btm_bar = findViewById(R.id.btm_nav_bar);

        btm_bar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.enrolled_courses) {
                    loadFrag(new course_enrolled());
                } else if (item.getItemId() == R.id.available_courses) {
                    loadFrag(new course_available());
                } else if (item.getItemId() == R.id.search) {
                    loadFrag(new student_search());
                } else if (item.getItemId() == R.id.profile) {
                    loadFrag(new student_profile());
                }
                return true;
            }
        });

        // Set the default selected item (optional)
        btm_bar.setSelectedItemId(R.id.enrolled_courses);
    }

    public void loadFrag(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(R.id.student_fragement_viewer, fragment);
        fragmentTransaction.commit();
    }
}
