package com.picsart.studio.Instructor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.picsart.studio.DBHelper.FirebaseHelper;
import com.picsart.studio.Models.Quiz;
import com.picsart.studio.Models.Quiz_Attempts;
import com.picsart.studio.Models.User;
import com.picsart.studio.R;
import java.util.List;

public class QuizPerformedAdapter extends RecyclerView.Adapter<QuizPerformedAdapter.MyViewHolder> {

    private Context context ;
    public List<Quiz_Attempts> mData ;
    private String course_name, quiz_name;


    public QuizPerformedAdapter(Context context, List<Quiz_Attempts> mData, String course_name) {
        this.context = context;
        this.mData = mData;
        this.quiz_name = quiz_name;
        this.course_name = course_name;
    }

    @Override
    public QuizPerformedAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.quiz_performed_cardview,parent,false);
        return new QuizPerformedAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
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
        holder.score.setText("Scores: "+mData.get(position).getScore());
        firebaseHelper.getUserByID(mData.get(position).getUser_id()).addOnCompleteListener(l->{
            if (l.isComplete() && l.isSuccessful()){
                User user = l.getResult();
                if (user != null) {
                    holder.user_id.setText(user.getName());
                } else {
                    holder.user_id.setText("Quiz Not Found");
                }
            } else {
                holder.user_id.setText("Error Fetching Quiz");
            }
        });
    }

    public void setData(List<Quiz_Attempts> enrolled_courses) {
        this.mData.clear(); // Clear the existing data
        this.mData.addAll(enrolled_courses); // Add the new data
        notifyDataSetChanged(); // Notify the adapter of the data change
    }

    @Override
    public int getItemCount() {
        try{
            return mData.size();
        }
        catch (Exception e){
            return 0;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView quiz_name, score, user_id;
        CardView cardview_content;

        public MyViewHolder(View itemView) {
            super(itemView);
            quiz_name = itemView.findViewById(R.id.quiz_name_at_performed);
            score = itemView.findViewById(R.id.score_at_performed);
            user_id = itemView.findViewById(R.id.user_id_at_performed);
            cardview_content = itemView.findViewById(R.id.quiz_performed_card);
        }
    }
}