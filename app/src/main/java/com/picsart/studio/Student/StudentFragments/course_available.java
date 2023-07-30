package com.picsart.studio.Student.StudentFragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.picsart.studio.Student.Adapter.Available_Course_Adapter;
import com.picsart.studio.DBHelper.FirebaseHelper;
import com.picsart.studio.Models.Course;
import com.picsart.studio.R;

import java.util.ArrayList;
import java.util.List;

public class course_available extends Fragment {
    List<Course> available_courses;
    String id, username, image, badge,dob;
    TextView name;
    FirebaseHelper firebaseHelper;
    public course_available() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_course_available_fragment, container, false);
        available_courses = new ArrayList<>();
        firebaseHelper = new FirebaseHelper();
        SharedPreferences sh = getActivity().getSharedPreferences("student_data",MODE_PRIVATE);
        this.id = sh.getString("id","");
        this.username = sh.getString("name","");
        this.image = sh.getString("img", "");
        this.badge = sh.getString("badge","");
        this.dob = sh.getString("dob","");
        name = view.findViewById(R.id.username);
        name.setText("Welcome, "+username);
        RecyclerView availablerecycler = view.findViewById(R.id.recycler_view_course_available);
        Available_Course_Adapter availablecoursesAdapter = new Available_Course_Adapter(requireContext(), available_courses);
        firebaseHelper.getAllCoursesAvailable()
                .addOnCompleteListener(result -> {
                    if (result.isSuccessful()) {
                        available_courses = result.getResult();
                        availablecoursesAdapter.setData(available_courses);
                    }
                })
                .addOnFailureListener(l -> {
                    Toast.makeText(requireContext(), "Check internet connection.", Toast.LENGTH_SHORT).show();
                });
        availablerecycler.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        availablerecycler.setAdapter(availablecoursesAdapter);
        return view;
    }
}