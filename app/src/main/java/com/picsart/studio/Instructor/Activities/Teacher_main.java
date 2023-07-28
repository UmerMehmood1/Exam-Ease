package com.picsart.studio.Instructor.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.picsart.studio.Instructor.InstructorFragments.Teacher_Profile_Fragment;
import com.picsart.studio.Instructor.InstructorFragments.Teacher_Search_Fragment;
import com.picsart.studio.Instructor.InstructorFragments.course_leading;
import com.picsart.studio.Student.StudentFragments.student_profile;
import com.picsart.studio.Student.StudentFragments.student_search;
import com.picsart.studio.R;

public class Teacher_main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);
        SharedPreferences sh = getSharedPreferences("teacher_data",MODE_PRIVATE);
        String teacher_id = sh.getString("id","");
        String name = sh.getString("name","");
        String image = sh.getString("img","");
        String badge = sh.getString("badge","");
        String dob = sh.getString("dob","");
        Toast.makeText(this, teacher_id, Toast.LENGTH_SHORT).show();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.instructor_fragement_viewer, new course_leading())
                    .commit();
        }
        FloatingActionButton add_course = findViewById(R.id.add_course_fb);
        BottomNavigationView btm_bar = findViewById(R.id.instructor_btm_nav_bar);
        add_course.setOnClickListener(l->{
            Intent intent = new Intent(this, Add_Course.class);
            intent.putExtra("id", teacher_id);
            startActivity(intent);
        });

        btm_bar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.courses_leading) {
                    loadFrag( new course_leading());
                    add_course.setVisibility(View.VISIBLE);
                } else if (item.getItemId() == R.id.search) {
                    add_course.setVisibility(View.GONE);
                    loadFrag(new Teacher_Search_Fragment());
                } else if (item.getItemId() == R.id.profile) {
                    add_course.setVisibility(View.GONE);
                    loadFrag(new Teacher_Profile_Fragment());
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
        fragmentTransaction.replace(R.id.instructor_fragement_viewer, fragment);
        fragmentTransaction.commit();
    }
}