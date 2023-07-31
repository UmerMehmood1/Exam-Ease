package com.picsart.studio.Student.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.picsart.studio.DBHelper.FirebaseHelper;
import com.picsart.studio.Models.Quiz;
import com.picsart.studio.Models.Quiz_Attempts;
import com.picsart.studio.R;
import com.picsart.studio.Student.Activities.QuizActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.picsart.studio.Models.Question;
import com.picsart.studio.Models.Quiz;
import com.picsart.studio.R;
import com.picsart.studio.Student.Activities.QuizActivity;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class QuizAttemptedAdapter extends RecyclerView.Adapter<QuizAttemptedAdapter.MyViewHolder> {
    private Context context;
    public List<Quiz_Attempts> mData;
    String course_id, Course_name;

    public QuizAttemptedAdapter(Context context, List<Quiz_Attempts> mData) {
        this.context = context;
        this.mData = mData;
    }

    @Override
    public QuizAttemptedAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.quiz_performed_cardview, parent, false);
        return new QuizAttemptedAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuizAttemptedAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        firebaseHelper.getQuizByID_but_no_Questions(mData.get(position).getQuiz_id()).addOnCompleteListener(l->{
            if (l.isComplete() && l.isSuccessful()){
                Quiz quiz = l.getResult();
                if (quiz != null) {
                    holder.quiz_name.setText(quiz.getQuizName());
                } else {
                    holder.quiz_name.setText("Quiz Not Found");
                }
            } else {
                holder.quiz_name.setText("Error Fetching Quiz");
            }
        });
        holder.score.setText("Scored: "+mData.get(position).getScore());
        holder.user_id.setText("");
    }

    public void setData(List<Quiz_Attempts> enrolled_courses) {
        if (this.mData == null) {
            this.mData = new ArrayList<>(); // Create a new list if mData is null
        } else {
            this.mData.clear(); // Clear the existing data
        }
        this.mData.addAll(enrolled_courses); // Add the new data
        notifyDataSetChanged();
        ;
    }

    @Override
    public int getItemCount() {
        try {
            return mData.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView quiz_name, score, user_id;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            quiz_name = itemView.findViewById(R.id.quiz_name_at_performed);
            user_id = itemView.findViewById(R.id.user_id_at_performed);
            score = itemView.findViewById(R.id.score_at_performed);
            cardView = itemView.findViewById(R.id.quiz_performed_card);


        }
    }
}