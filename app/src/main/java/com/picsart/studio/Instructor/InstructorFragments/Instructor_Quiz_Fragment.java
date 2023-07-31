package com.picsart.studio.Instructor.InstructorFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.picsart.studio.DBHelper.FirebaseHelper;
import com.picsart.studio.Instructor.Adapter.QuizAdapterAtLeadingCourse;
import com.picsart.studio.Models.Quiz;
import com.picsart.studio.R;
import com.picsart.studio.Student.Adapter.QuizAdapter;
import com.picsart.studio.Student.StudentFragments.course_enrolled;

import java.util.ArrayList;
import java.util.List;

public class Instructor_Quiz_Fragment extends Fragment {
    String course_id, Title;
    private List<Quiz> quizzes;
    private RecyclerView recyclerView;
    FirebaseHelper firebaseHelper;


    public Instructor_Quiz_Fragment(String Title,String course_id) {
        this.Title = Title;
        this.course_id = course_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.instructor_quiz_view_fragment, container, false);
        quizzes = new ArrayList<>();
        firebaseHelper = new FirebaseHelper();
        recyclerView = view.findViewById(R.id.recycler_view_for_quiz_in_course_enrolled);
        String teacher_id= requireContext().getSharedPreferences("teacher_data",Context.MODE_PRIVATE).getString("id","");
        Toast.makeText(requireContext(), course_id, Toast.LENGTH_SHORT).show();
        QuizAdapterAtLeadingCourse quizAdapterAtLeadingCourse = new QuizAdapterAtLeadingCourse(requireContext(), quizzes, teacher_id, course_id, Title);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(quizAdapterAtLeadingCourse);
        firebaseHelper.getQuizzesByCourseId(course_id).addOnCompleteListener(l -> {
            if (l.isSuccessful()) {
                quizAdapterAtLeadingCourse.mData = l.getResult();
                quizAdapterAtLeadingCourse.notifyDataSetChanged();
            } else {
                Toast.makeText(requireContext(), "Failed to fetch quizzes", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
