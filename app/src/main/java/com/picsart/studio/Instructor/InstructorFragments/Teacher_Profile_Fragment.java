package com.picsart.studio.Instructor.InstructorFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.picsart.studio.AboutActivity;
import com.picsart.studio.Login_and_sign_up.Activities.login;
import com.picsart.studio.R;
import com.picsart.studio.Student.StudentFragments.course_enrolled;

public class Teacher_Profile_Fragment extends Fragment {
    ImageButton back;
    String username, badge, image, teacher_id, dob;
    TextView name, badge_if_any;
    ImageView profile_img;
    ConstraintLayout Log_out, about;
    MeowBottomNavigation btm_bar;

    public Teacher_Profile_Fragment() {
    }
    public Teacher_Profile_Fragment(MeowBottomNavigation btm_bar) {
        this.btm_bar = btm_bar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_profile_fragment, container, false);

        Log_out = view.findViewById(R.id.log_out_at_profile);
        about = view.findViewById(R.id.about_at_profile);


        SharedPreferences sh = getActivity().getSharedPreferences("teacher_data", Context.MODE_PRIVATE);
        this.teacher_id = sh.getString("id", "");
        this.username = sh.getString("name", "");
        this.image = sh.getString("img", "");
        this.badge = sh.getString("badge", "");
        this.dob = sh.getString("dob", "");
        name = view.findViewById(R.id.username);
        name.setText(username);
        profile_img = view.findViewById(R.id.user_profile_img);
        profile_img.setImageResource(R.drawable.img_1_intro);
//        profile_img.setImageResource(Integer.valueOf(image));
        badge_if_any = view.findViewById(R.id.badge);
        badge_if_any.setText(badge);

        back = view.findViewById(R.id.back);
        back.setOnClickListener(l -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.instructor_fragement_viewer, new course_leading())
                    .commit();
            btm_bar.show(1, true);
        });
        Log_out.setOnClickListener(l->{
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("teacher_data", Context.MODE_PRIVATE);
            SharedPreferences.Editor teacher_editor = sharedPreferences.edit();
            teacher_editor.clear();
            teacher_editor.apply();
            getActivity().finishAffinity();
            FirebaseAuth.getInstance().signOut();
        });
        about.setOnClickListener(l->{
            startActivity(new Intent(requireContext(), AboutActivity.class));
        });
        return view;
    }

}