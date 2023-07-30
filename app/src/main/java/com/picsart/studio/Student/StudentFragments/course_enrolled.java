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

import com.picsart.studio.Student.Adapter.Course_Enrolled_Adapter;
import com.picsart.studio.DBHelper.FirebaseHelper;
import com.picsart.studio.Models.Course;
import com.picsart.studio.R;

import java.util.ArrayList;
import java.util.List;

public class course_enrolled extends Fragment {
    List<Course> enrolled_courses;
    TextView username;
    String name,id,badge,dob;
    String image;
    FirebaseHelper firebaseHelper;
    public course_enrolled() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_course_enrolled_fragment, container, false);
        firebaseHelper = new FirebaseHelper();
        enrolled_courses = new ArrayList<>();
        SharedPreferences sh = getActivity().getSharedPreferences("student_data",MODE_PRIVATE);
        this.id = sh.getString("id","");
        this.name = sh.getString("name","");
        this.image = sh.getString("img", "");
        this.badge = sh.getString("badge","");
        this.dob = sh.getString("dob","");
        RecyclerView enrolledrecycler = view.findViewById(R.id.recycler_view_course_available);
        Course_Enrolled_Adapter enrolledcoursesAdapter = new Course_Enrolled_Adapter(requireContext(), enrolled_courses);
        enrolledrecycler.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        enrolledrecycler.setAdapter(enrolledcoursesAdapter);
        //Getting course for student
        firebaseHelper.getCoursesForStudent(id).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                enrolled_courses = task.getResult();
                enrolledcoursesAdapter.setData(enrolled_courses);
            } else {
                Toast.makeText(requireContext(), "Check internet connection", Toast.LENGTH_SHORT).show();
        }
    });

        username = view.findViewById(R.id.username);
        username.setText("Welcome, " + name);

        return view;
    }
}