package com.picsart.studio.Student.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.picsart.studio.Student.Adapter.QuizAdapter;
import com.picsart.studio.DBHelper.FirebaseHelper;
import com.picsart.studio.Models.Quiz;
import com.picsart.studio.R;

import java.util.ArrayList;
import java.util.List;

public class Student_Course_Enrolled_Activity extends AppCompatActivity {

    private TextView tvtitle,tvdescription,tvcategory;
    private ImageView img;
    private ImageButton back;
    private RecyclerView recyclerView;
    private List<Quiz> quizzes;
    private FirebaseHelper firebaseHelper;
    private String course_id;
    int image;
    String Title;
    String Description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_course_details_enrolled);
        quizzes = new ArrayList<>();
        firebaseHelper = new FirebaseHelper();

        Intent intent = getIntent();
        course_id = intent.getStringExtra("course_id");
        Title = intent.getStringExtra("content_name");
        Description = intent.getStringExtra("content_description");
        image = intent.getIntExtra("Content_type_img", 0);
        Toast.makeText(this, course_id, Toast.LENGTH_SHORT).show();
        tvtitle = findViewById(R.id.txttitle);
        tvdescription = findViewById(R.id.txtDesc);
        tvcategory = findViewById(R.id.txtCat);
        img = findViewById(R.id.bookthumbnail);
        back = findViewById(R.id.back_btn);
        back.setOnClickListener(l->{
            finish();
        });
        recyclerView = findViewById(R.id.recycler_view_for_quiz_in_course_enrolled);
        QuizAdapter quizAdapter = new QuizAdapter(this, quizzes, course_id,Title);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(quizAdapter);

        firebaseHelper.getQuizzesByCourseId(course_id).addOnCompleteListener(l -> {
            if (l.isSuccessful()) {
                quizAdapter.mData = l.getResult();
                quizAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Failed to fetch quizzes", Toast.LENGTH_SHORT).show();
            }
        });
        tvtitle.setText(Title);
        tvdescription.setText(Description);
        img.setImageResource(image);
    }
}