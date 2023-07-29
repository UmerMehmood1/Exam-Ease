package com.picsart.studio.Instructor.InstructorFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.picsart.studio.Instructor.Adapter.Instructor_Course_Leading_Adapter;
import com.picsart.studio.DBHelper.FirebaseHelper;
import com.picsart.studio.Models.Course;
import com.picsart.studio.R;

import java.util.ArrayList;
import java.util.List;

public class course_leading extends Fragment {
    List<Course> course_leading;
    TextView username;
    String name;
    String teacher_id;
    Instructor_Course_Leading_Adapter course_leading_adapter;
    FirebaseHelper firebaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.instructor_course_leading_fragment, container, false);

        // Initialize the enrolled_courses list here
        course_leading = new ArrayList<>();

        // Get shared preferences inside the onCreateView() method
        SharedPreferences sh = requireActivity().getSharedPreferences("teacher_data", Context.MODE_PRIVATE);
        teacher_id = sh.getString("id","");
        name = sh.getString("name","");
        String image = sh.getString("img","");
        String badge = sh.getString("badge","");
        String dob = sh.getString("dob","");

        firebaseHelper = new FirebaseHelper();

        RecyclerView course_leading_recycler_view = view.findViewById(R.id.recycler_view_course_leading);
        course_leading_adapter = new Instructor_Course_Leading_Adapter(requireContext(), course_leading, teacher_id);
        course_leading_recycler_view.setLayoutManager(new LinearLayoutManager(requireContext()));
        course_leading_recycler_view.setAdapter(course_leading_adapter);
        add_data_to_course_leading();
        username = view.findViewById(R.id.username);
        username.setText("Welcome, " + name);
        return view;
    }

    public Instructor_Course_Leading_Adapter getAdapter(){
        return course_leading_adapter;
    }

    public void add_data_to_course_leading(){
        firebaseHelper.getCoursesByTeacherId(teacher_id).addOnCompleteListener(l->{
            course_leading_adapter.mData = l.getResult();
            course_leading_adapter.notifyDataSetChanged();
        });
    }
}
