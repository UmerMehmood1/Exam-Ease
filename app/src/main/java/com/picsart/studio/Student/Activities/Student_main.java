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
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.navigation.NavigationBarView;
import com.picsart.studio.Student.StudentFragments.course_enrolled;
import com.picsart.studio.Student.StudentFragments.course_available;
import com.picsart.studio.Student.StudentFragments.student_profile;
import com.picsart.studio.Student.StudentFragments.student_search;
import com.picsart.studio.R;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

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

        MeowBottomNavigation btm_bar = findViewById(R.id.btm_nav_bar);
        btm_bar.add(new MeowBottomNavigation.Model(1, R.drawable.baseline_home_24));
        btm_bar.add(new MeowBottomNavigation.Model(2, R.drawable.available_courses));
        btm_bar.add(new MeowBottomNavigation.Model(3, R.drawable.baseline_search_24));
        btm_bar.add(new MeowBottomNavigation.Model(4, R.drawable.baseline_account_circle_24));
        btm_bar.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                if (model.getId() == 1) {
                    loadFrag(new course_enrolled());
                } else if (model.getId() == 2) {
                    loadFrag(new course_available());
                } else if (model.getId() == 3) {
                    loadFrag(new student_search());
                } else if (model.getId() == 4) {
                    loadFrag(new student_profile(btm_bar));
                }
            return null;
            }
        });
        btm_bar.show(1, true);
    }
    public void loadFrag(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(R.id.student_fragement_viewer, fragment);
        fragmentTransaction.commit();
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finishAffinity();
    }
}
