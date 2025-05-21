package com.picsart.studio.Instructor.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.picsart.studio.DBHelper.FirebaseHelper;
import com.picsart.studio.Instructor.Activities.fragmentForCourseDetailsActivity.InstructorPastPaperFragment;
import com.picsart.studio.Instructor.Adapter.InstructorViewPagerAdapter;
import com.picsart.studio.Instructor.Adapter.QuizAdapterAtLeadingCourse;
import com.picsart.studio.Instructor.Activities.AddQuiz;
import com.picsart.studio.Instructor.InstructorFragments.Instructor_Quiz_Fragment;
import com.picsart.studio.Instructor.InstructorFragments.QuizPerformedFragment;
import com.picsart.studio.Models.Quiz;
import com.picsart.studio.R;
import com.picsart.studio.Student.Activities.fragmentForCourseDetailsActivity.InstructorCourseVideoFragment;

import java.util.ArrayList;
import java.util.List;

public class CourseQuizzes extends AppCompatActivity {

    String name, category, description, duration, course_id, teacher_id;
    int course_img;
    TextView course_title, course_category, course_description;
    List<Quiz> quizList;
    ImageButton back;
    FirebaseHelper firebaseHelper;
    FloatingActionButton floating_button_add_quiz;
    ViewPager2 myViewPager2;
    InstructorViewPagerAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructor_quiz_activity);
        quizList = new ArrayList<>();
        firebaseHelper = new FirebaseHelper();

        Intent get_intent = getIntent();
        this.teacher_id = getSharedPreferences("teacher_data", MODE_PRIVATE).getString("id", "");
        this.course_id = get_intent.getStringExtra("course_id");
        this.name = get_intent.getStringExtra("course_name");
        this.course_img = get_intent.getIntExtra("course_img", 0);
        this.category = get_intent.getStringExtra("course_category");
        this.description = get_intent.getStringExtra("course_description");
        this.duration = get_intent.getStringExtra("course_duration");

        course_title = findViewById(R.id.coursse_title_at_instructor_quiz_view);
        course_category = findViewById(R.id.course_category_at_instructor_quiz_view);
        course_description = findViewById(R.id.course_description_at_instructor_quiz_view);
        floating_button_add_quiz = findViewById(R.id.add_quiz_floating_button);
        back = findViewById(R.id.back_btn);

        back.setOnClickListener(l -> {
            finish();
        });

        course_title.setText(name);
        course_category.setText(category);
        course_description.setText(description);

        myViewPager2 = findViewById(R.id.viewpager_at_quiz_view);

        myAdapter = new InstructorViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        myAdapter.addFragment(new Instructor_Quiz_Fragment(name, course_id));
        myAdapter.addFragment(new QuizPerformedFragment(name, course_id));
        myAdapter.addFragment(new InstructorCourseVideoFragment(course_id));
        myAdapter.addFragment(new InstructorPastPaperFragment(course_id));
        myViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        myViewPager2.setAdapter(myAdapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, myViewPager2, (tab, position) -> {
            if (position == 0) {
                tab.setText("Quizzes");
            } else if (position == 1) {
                tab.setText("Results");
            } else if (position == 2) {
                tab.setText("Course Videos");
            } else if (position == 3) {
                tab.setText("Past Papers");
            }
        }).attach();
    }
}