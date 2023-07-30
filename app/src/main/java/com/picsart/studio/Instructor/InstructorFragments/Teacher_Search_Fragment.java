package com.picsart.studio.Instructor.InstructorFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.picsart.studio.Student.Adapter.Available_Course_Adapter;
import com.picsart.studio.DBHelper.FirebaseHelper;
import com.picsart.studio.Models.Course;
import com.picsart.studio.R;

import java.util.List;

public class Teacher_Search_Fragment extends Fragment {
    EditText searchvalue;
    RecyclerView recyclerView;
    FirebaseHelper firebaseHelper;
    List<Course> search_courses;
    String teacher_id;
    public Teacher_Search_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_search_fragment, container, false);
        searchvalue =view.findViewById(R.id.Search_value);
        recyclerView =view.findViewById(R.id.recycler_view_course);
        firebaseHelper = new FirebaseHelper();
        Available_Course_Adapter availablecoursesAdapter = new Available_Course_Adapter(requireContext(), search_courses);
        SharedPreferences sh = getActivity().getSharedPreferences("teacher_data", Context.MODE_PRIVATE);
        this.teacher_id = sh.getString("id","");
        String name = sh.getString("name","");
        String image = sh.getString("img","");
        String badge = sh.getString("badge","");
        String dob = sh.getString("dob","");
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerView.setAdapter(availablecoursesAdapter);
        searchvalue.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                firebaseHelper.findCoursesByName(searchvalue.getText().toString())
                        .addOnCompleteListener(result -> {
                            if (result.isSuccessful()) {
                                search_courses = result.getResult();
                                Toast.makeText(requireContext(), result.getResult().toString(), Toast.LENGTH_SHORT).show();
                                availablecoursesAdapter.setData(search_courses);
                            }
                        })
                        .addOnFailureListener(l -> {
                            Toast.makeText(requireContext(), "Check internet connection.", Toast.LENGTH_SHORT).show();
                        });
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }
}