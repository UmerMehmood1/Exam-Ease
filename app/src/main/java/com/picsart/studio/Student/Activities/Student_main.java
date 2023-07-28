package com.picsart.studio.Student.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String image = intent.getStringExtra("img");
        String badge = intent.getStringExtra("badge");
        String dob = intent.getStringExtra("dob");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.student_fragement_viewer, new course_enrolled(id,name,image,badge, dob))
                    .commit();
        }

        BottomNavigationView btm_bar = findViewById(R.id.btm_nav_bar);

        btm_bar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.enrolled_courses) {
                    loadFrag(new course_enrolled(id, name, image, badge, dob));
                } else if (item.getItemId() == R.id.available_courses) {
                    loadFrag(new course_available(id,name, image, badge, dob));
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
