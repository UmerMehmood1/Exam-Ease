package com.picsart.studio.Student.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.picsart.studio.R;
import com.picsart.studio.Student.StudentFragments.course_available;
import com.picsart.studio.Student.StudentFragments.course_enrolled;
import com.picsart.studio.Student.StudentFragments.student_profile;
import com.picsart.studio.Student.StudentFragments.student_search;

public class Student_main extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_interface);

        if (savedInstanceState == null) {
            loadFrag(new course_enrolled());
        }

        bottomNavigationView = findViewById(R.id.btm_nav_bar);
        bottomNavigationView.setOnItemSelectedListener(navListener);
    }

    private final NavigationBarView.OnItemSelectedListener navListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();
            if(itemId == R.id.enrolled_courses) {
                selectedFragment = new course_enrolled();
            }
            else if(itemId == R.id.available_courses) {
                selectedFragment = new course_available();
            }
            else if(itemId == R.id.search) {
                selectedFragment = new student_search();
            }
            else if(itemId == R.id.profile) {
                selectedFragment = new student_profile(bottomNavigationView);
            }
            if (selectedFragment != null) {
                loadFrag(selectedFragment);
                return true;
            }
            return false;
        }
    };

    public void loadFrag(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(R.id.student_fragement_viewer, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
