package com.picsart.studio.Student.StudentFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.picsart.studio.R;
public class student_profile extends Fragment {
    ImageButton back;
    String username, badge, image, teacher_id, dob;
    TextView name,badge_if_any;
    ImageView profile_img;
    public student_profile() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_profile_fragment, container, false);
        name = view.findViewById(R.id.username);
        name.setText(username);

        SharedPreferences sh = getActivity().getSharedPreferences("student_data", Context.MODE_PRIVATE);
        this.teacher_id = sh.getString("id","");
        this.username = sh.getString("name","");
        this.image = sh.getString("img","");
        this.badge = sh.getString("badge","");
        this.dob = sh.getString("dob","");

        profile_img = view.findViewById(R.id.user_profile_img);
        profile_img.setImageResource(Integer.valueOf(image));
        badge_if_any = view.findViewById(R.id.badge);
        badge_if_any.setText(badge);

        back = view.findViewById(R.id.back);
                back.setOnClickListener(l->{
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.student_fragement_viewer, new course_enrolled())
                            .commit();
                });
        return view;
    }
}