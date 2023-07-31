package com.picsart.studio.Instructor.InstructorFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.picsart.studio.DBHelper.FirebaseHelper;
import com.picsart.studio.Instructor.Adapter.QuizPerformedAdapter;
import com.picsart.studio.Models.Quiz;
import com.picsart.studio.Models.Quiz_Attempts;
import com.picsart.studio.R;
import com.picsart.studio.Student.Adapter.QuizAdapter;

import java.util.ArrayList;
import java.util.List;

public class QuizPerformedFragment extends Fragment {
    private List<Quiz_Attempts> quizzes;
    private RecyclerView recyclerView;
    FirebaseHelper firebaseHelper;
    String course_name, course_id;
    QuizPerformedAdapter quizPerformedAdapter;
    public QuizPerformedFragment(String course_name, String course_id) {
        this.course_name = course_name;
        this.course_id = course_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.instructor_fragment_quiz_performed, container, false);
        quizzes = new ArrayList<>();
        firebaseHelper = new FirebaseHelper();
        recyclerView = view.findViewById(R.id.recycler_view_for_quiz_performed_in_course_enrolled);
        quizPerformedAdapter = new QuizPerformedAdapter(requireContext(), quizzes, course_name);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(quizPerformedAdapter);
        add_data_to_course_leading();
        return view;
    }
    public void add_data_to_course_leading(){
        firebaseHelper.getQuizAttemptsByCourseId(course_id).addOnCompleteListener(l -> {
            if (l.isSuccessful()) {
                quizPerformedAdapter.mData = l.getResult();
                quizPerformedAdapter.notifyDataSetChanged();
            } else {
                Log.d("Quizzes at QuizPerformed Fragment:", l.getException().toString());
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        add_data_to_course_leading();
    }
}
