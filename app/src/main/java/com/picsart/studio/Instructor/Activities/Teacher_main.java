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

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.picsart.studio.Instructor.InstructorFragments.Teacher_Profile_Fragment;
import com.picsart.studio.Instructor.InstructorFragments.Teacher_Search_Fragment;
import com.picsart.studio.Instructor.InstructorFragments.course_leading;
import com.picsart.studio.Student.StudentFragments.course_available;
import com.picsart.studio.Student.StudentFragments.course_enrolled;
import com.picsart.studio.Student.StudentFragments.student_profile;
import com.picsart.studio.Student.StudentFragments.student_search;
import com.picsart.studio.R;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

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
        MeowBottomNavigation btm_bar = findViewById(R.id.instructor_btm_nav_bar);
        btm_bar.add(new MeowBottomNavigation.Model(1, R.drawable.baseline_home_24));
        btm_bar.add(new MeowBottomNavigation.Model(2, R.drawable.baseline_search_24));
        btm_bar.add(new MeowBottomNavigation.Model(3, R.drawable.baseline_account_circle_24));
        btm_bar.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                if (model.getId() == 1) {
                    loadFrag(new course_leading());
                    add_course.setVisibility(View.VISIBLE);
                } else if (model.getId() == 2) {
                    loadFrag(new Teacher_Search_Fragment());
                    add_course.setVisibility(View.GONE);
                } else if (model.getId() == 3) {
                    loadFrag(new Teacher_Profile_Fragment());
                    add_course.setVisibility(View.GONE);
                }
                return null;
            }
        });
        btm_bar.show(1, true);

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