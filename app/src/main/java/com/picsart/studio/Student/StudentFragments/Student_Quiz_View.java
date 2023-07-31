package com.picsart.studio.Student.StudentFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.picsart.studio.DBHelper.FirebaseHelper;
import com.picsart.studio.Models.Quiz;
import com.picsart.studio.R;
import com.picsart.studio.Student.Adapter.QuizAdapter;

import java.util.List;

public class Student_Quiz_View extends Fragment {
    RecyclerView recyclerView;
    List<Quiz> quizList;
    String course_id, course_name;
    FirebaseHelper firebaseHelper;
    public Student_Quiz_View(String course_id, String course_name) {
        this.course_id = course_id;
        this.course_name = course_name;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firebaseHelper = new FirebaseHelper();
        View view = inflater.inflate(R.layout.student_fragment_quiz_view, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_for_quiz_performed_in_course_enrolled_student);
        QuizAdapter quizAdapter = new QuizAdapter(requireContext(),quizList,course_id,course_name);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(quizAdapter);
        firebaseHelper.getQuizzesByCourseId(course_id).addOnCompleteListener(l -> {
            if (l.isSuccessful()) {
                quizAdapter.mData = l.getResult();
                quizAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(requireContext(), "Failed to fetch quizzes", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}