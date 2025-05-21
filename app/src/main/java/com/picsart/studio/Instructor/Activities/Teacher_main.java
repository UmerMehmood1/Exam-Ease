package com.picsart.studio.Instructor.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.picsart.studio.Instructor.InstructorFragments.Teacher_Profile_Fragment;
import com.picsart.studio.Instructor.InstructorFragments.Teacher_Search_Fragment;
import com.picsart.studio.Instructor.InstructorFragments.course_leading;
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
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.instructor_fragement_viewer, new course_leading())
                    .commit();
        }
        FloatingActionButton add_course = findViewById(R.id.add_course_fb);
        add_course.setOnClickListener(l->{
            Intent intent = new Intent(this, Add_Course.class);
            intent.putExtra("id", teacher_id);
            startActivity(intent);
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.instructor_btm_nav_bar);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.courses_leading) {
                    selectedFragment = new course_leading();
                    add_course.setVisibility(View.VISIBLE);
                } else if (itemId == R.id.search) {
                    selectedFragment = new Teacher_Search_Fragment();
                    add_course.setVisibility(View.GONE);
                } else if (itemId == R.id.profile) {
                    selectedFragment = new Teacher_Profile_Fragment(bottomNavigationView);
                    add_course.setVisibility(View.GONE);
                }

                if (selectedFragment != null) {
                    loadFrag(selectedFragment);
                }
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.courses_leading); // Default


    }

    public void loadFrag(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(R.id.instructor_fragement_viewer, fragment);
        fragmentTransaction.commit();
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finishAffinity();
    }
}