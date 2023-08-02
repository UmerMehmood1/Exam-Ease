package com.picsart.studio.Student.StudentFragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.picsart.studio.DBHelper.FirebaseHelper;
import com.picsart.studio.Models.Course;
import com.picsart.studio.Models.Quiz_Attempts;
import com.picsart.studio.R;
import com.picsart.studio.Student.Adapter.QuizAdapter;
import com.picsart.studio.Student.Adapter.QuizAttemptedAdapter;

import java.util.ArrayList;
import java.util.List;

public class Student_Quiz_Attempt_Fragment extends Fragment {
    RecyclerView recyclerView;
    List<Quiz_Attempts> quizAttemptsList = new ArrayList<>();
    FirebaseHelper firebaseHelper;
    QuizAttemptedAdapter quizAdapter;
    String course_id;
    public Student_Quiz_Attempt_Fragment(String course_id) {
        this.course_id = course_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firebaseHelper = new FirebaseHelper();
        View view = inflater.inflate(R.layout.student_fragment_quiz_attempt, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_for_quiz_attempted_at_course_enrolled);
        quizAdapter = new QuizAttemptedAdapter(requireContext(),quizAttemptsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(quizAdapter);
        add_data_to_course_leading();
        return view;

    }
    public void add_data_to_course_leading(){
        String student_id = requireContext().getSharedPreferences("student_data", Context.MODE_PRIVATE).getString("id","");
        firebaseHelper.getQuizAttemptsByUserId(student_id, course_id).addOnCompleteListener(l -> {
            if (l.isSuccessful()) {
                quizAdapter.mData = l.getResult();
                quizAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(requireContext(), "Failed to fetch quizzes", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        add_data_to_course_leading();
    }
}