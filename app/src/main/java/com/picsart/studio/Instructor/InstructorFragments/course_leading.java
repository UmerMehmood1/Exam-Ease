package com.picsart.studio.Instructor.InstructorFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.picsart.studio.Instructor.Adapter.Instructor_Course_Leading_Adapter;
import com.picsart.studio.DBHelper.FirebaseHelper;
import com.picsart.studio.Models.Course;
import com.picsart.studio.R;

import java.util.ArrayList;
import java.util.List;

public class course_leading extends Fragment {
    List<Course> enrolled_courses;
    TextView username;
    String name;
    String teacher_id;
    Instructor_Course_Leading_Adapter course_leading_adapter;
    FirebaseHelper firebaseHelper;
    public course_leading() {
        SharedPreferences sh = getActivity().getSharedPreferences("teacher_data", Context.MODE_PRIVATE);
        this.teacher_id = sh.getString("id","");
        this.name = sh.getString("name","");
        String image = sh.getString("img","");
        String badge = sh.getString("badge","");
        String dob = sh.getString("dob","");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Toast.makeText(requireContext(), teacher_id, Toast.LENGTH_SHORT).show();
        View view = inflater.inflate(R.layout.instructor_course_leading_fragment, container, false);
        firebaseHelper = new FirebaseHelper();

        // Initialize the enrolled_courses list here
        enrolled_courses = new ArrayList<>();

        RecyclerView course_leading_recycler_view = view.findViewById(R.id.recycler_view_course_leading);
        course_leading_adapter = new Instructor_Course_Leading_Adapter(requireContext(), enrolled_courses, teacher_id);
        course_leading_recycler_view.setLayoutManager(new LinearLayoutManager(requireContext()));
        course_leading_recycler_view.setAdapter(course_leading_adapter);

        firebaseHelper.GetCoursesCreatedByUserId(teacher_id)
                .addOnCompleteListener(result -> {
                    if (result.isSuccessful()) {
                        enrolled_courses = result.getResult();
                        course_leading_adapter.setData(enrolled_courses);
                    }
                })
                .addOnFailureListener(l -> {
                    Toast.makeText(requireContext(), "Check internet connection.", Toast.LENGTH_SHORT).show();
                });

        username = view.findViewById(R.id.username);
        username.setText("Welcome, " + name);
        return view;
    }
    public Instructor_Course_Leading_Adapter getAdapter(){
        return course_leading_adapter;
    }
}