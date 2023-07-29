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

import androidx.fragment.app.Fragment;

import com.picsart.studio.R;
import com.picsart.studio.Student.StudentFragments.course_enrolled;

public class Teacher_Profile_Fragment extends Fragment {
    ImageButton back;
    String username, badge, image, teacher_id, dob;
    TextView name, badge_if_any;
    ImageView profile_img;

    public Teacher_Profile_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_profile_fragment, container, false);
        name = view.findViewById(R.id.username);
        name.setText(username);
        SharedPreferences sh = getActivity().getSharedPreferences("teacher_data", Context.MODE_PRIVATE);
        this.teacher_id = sh.getString("id", "");
        this.username = sh.getString("name", "");
        this.image = sh.getString("img", "");
        this.badge = sh.getString("badge", "");
        this.dob = sh.getString("dob", "");
        profile_img = view.findViewById(R.id.user_profile_img);
        profile_img.setImageResource(Integer.valueOf(image));
        badge_if_any = view.findViewById(R.id.badge);
        badge_if_any.setText(badge);

        back = view.findViewById(R.id.back);
        back.setOnClickListener(l -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.instructor_fragement_viewer, new course_enrolled())
                    .commit();
        });
        return view;
    }
}