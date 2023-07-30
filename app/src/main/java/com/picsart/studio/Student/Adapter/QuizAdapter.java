package com.picsart.studio.Student.Adapter;
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
public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.MyViewHolder> {
    private Context context ;
    public List<Quiz> mData ;
    String course_id, Course_name;
    public QuizAdapter(Context context, List<Quiz> mData, @Nullable String course_id, String Course_name) {
        this.course_id = course_id;
        this.context = context;
        this.mData = mData;
        this.Course_name = Course_name;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_quiz_cardview,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position){
        holder.quiz_title.setText(mData.get(position).getQuizName());
        holder.course_title_at_quiz_card.setText(Course_name);
        holder.total_questions.setText(String.valueOf(mData.get(position).getTotalQuestions()));
        holder.cardView.setOnClickListener(l->{
            Intent intent = new Intent(context, QuizActivity.class);
            intent.putExtra("selectedQuiz", mData.get(position));
            context.startActivity(intent);
        });

    }
    public void setData(List<Quiz> enrolled_courses) {
        if (this.mData == null) {
            this.mData = new ArrayList<>(); // Create a new list if mData is null
        } else {
            this.mData.clear(); // Clear the existing data
        }
        this.mData.addAll(enrolled_courses); // Add the new data
        notifyDataSetChanged();;
    }
    @Override
    public int getItemCount() {
        try {
            return mData.size();
        }
        catch (Exception e){
            return 0;
        }
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView quiz_title,course_title_at_quiz_card, total_questions;
        CardView cardView ;
        ImageButton start_quiz_at_card;

        public MyViewHolder(View itemView) {
            super(itemView);
            quiz_title = itemView.findViewById(R.id.quiz_title) ;
            course_title_at_quiz_card = itemView.findViewById(R.id.course_title_at_quiz_card);
            total_questions = itemView.findViewById(R.id.total_questions);
            cardView = itemView.findViewById(R.id.quiz_card);
            start_quiz_at_card = itemView.findViewById(R.id.start_quiz_at_card);


        }
    }
}