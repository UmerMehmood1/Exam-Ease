package com.picsart.studio.Student.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.picsart.studio.Instructor.Adapter.InstructorViewPagerAdapter;
import com.picsart.studio.Instructor.Adapter.Instructor_Course_Leading_Adapter;
import com.picsart.studio.Instructor.InstructorFragments.Instructor_Quiz_Fragment;
import com.picsart.studio.Instructor.InstructorFragments.QuizPerformedFragment;
import com.picsart.studio.Student.Adapter.QuizAdapter;
import com.picsart.studio.DBHelper.FirebaseHelper;
import com.picsart.studio.Models.Quiz;
import com.picsart.studio.R;
import com.picsart.studio.Student.StudentFragments.Student_Quiz_Attempt_Fragment;
import com.picsart.studio.Student.StudentFragments.Student_Quiz_View;

import java.util.ArrayList;
import java.util.List;

public class Student_Course_Enrolled_Activity extends AppCompatActivity {

    private TextView tvtitle,tvdescription,tvcategory;
    private ImageView img;
    private ImageButton back;
    private FirebaseHelper firebaseHelper;
    private String course_id;
    int image;
    String Title;
    String Description;
    ViewPager2 myViewPager2;
    InstructorViewPagerAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_course_details_enrolled);
        firebaseHelper = new FirebaseHelper();
        Intent intent = getIntent();
        course_id = intent.getStringExtra("course_id");
        Title = intent.getStringExtra("content_name");
        Description = intent.getStringExtra("content_description");
        image = intent.getIntExtra("Content_type_img", 0);
        tvtitle = findViewById(R.id.txttitle);
        tvdescription = findViewById(R.id.txtDesc);
        tvcategory = findViewById(R.id.txtCat);
        img = findViewById(R.id.bookthumbnail);
        back = findViewById(R.id.back_btn);
        back.setOnClickListener(l->{
            finish();
        });


        myViewPager2 = findViewById(R.id.viewpager_at_student_quiz_view);
        myAdapter = new InstructorViewPagerAdapter(getSupportFragmentManager(), getLifecycle());

        // add Fragments in your ViewPagerFragmentAdapter class
        myAdapter.addFragment(new Student_Quiz_View(course_id, Title));
        myAdapter.addFragment(new Student_Quiz_Attempt_Fragment());

        myViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        myViewPager2.setAdapter(myAdapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout); // Make sure you have the correct ID here
        new TabLayoutMediator(tabLayout, myViewPager2,
                (tab, position) -> {
                    // Set the tab text here based on the position
                    if (position == 0) {
                        tab.setText("Quizzes");
                    } else if (position == 1) {
                        tab.setText("Results");
                    }
                }
        ).attach();

        tvtitle.setText(Title);
        tvdescription.setText(Description);
        img.setImageResource(image);
    }
}