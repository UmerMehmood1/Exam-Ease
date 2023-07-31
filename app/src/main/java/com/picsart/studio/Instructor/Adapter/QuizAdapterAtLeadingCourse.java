package com.picsart.studio.Instructor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.picsart.studio.Instructor.Activities.AddQuiz;
import com.picsart.studio.Instructor.Activities.UpdateQuiz;
import com.picsart.studio.Instructor.CustomDialogBox.QuizLongClickListenerDialog;
import com.picsart.studio.Models.Quiz;
import com.picsart.studio.R;
import java.util.List;

public class QuizAdapterAtLeadingCourse extends RecyclerView.Adapter<QuizAdapterAtLeadingCourse.MyViewHolder> {

        private Context context ;
        public List<Quiz> mData ;
        private String course_id ;
        private String teacher_id ;
        private String course_name;


        public QuizAdapterAtLeadingCourse(Context context, List<Quiz> mData, String teacher_id,String course_id, String course_name) {
            this.context = context;
            this.mData = mData;
            this.course_id = course_id;
            this.teacher_id = teacher_id;
            this.course_name = course_name;
        }

        @Override
        public QuizAdapterAtLeadingCourse.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.instructor_quiz_cardview,parent,false);
            return new QuizAdapterAtLeadingCourse.MyViewHolder(view);
        }

        public void setData(List<Quiz> enrolled_courses) {
            this.mData.clear(); // Clear the existing data
            this.mData.addAll(enrolled_courses); // Add the new data
            notifyDataSetChanged(); // Notify the adapter of the data change
        }
        @Override
        public void onBindViewHolder(@NonNull QuizAdapterAtLeadingCourse.MyViewHolder holder, int position) {

            holder.quiz_title.setText(mData.get(position).getQuizName());
            holder.course_name.setText(course_name);
            holder.total_questions.setText(String.valueOf(mData.get(position).getTotalQuestions()));
            holder.cardview_content.setOnLongClickListener(l ->{
                QuizLongClickListenerDialog quizLongClickListenerDialog = new QuizLongClickListenerDialog(context, mData.get(position).getQuizId(),mData.get(position).getCourseId());
                quizLongClickListenerDialog.show();
                return false;
            });



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

            TextView quiz_title, course_name, total_questions;
            CardView cardview_content;

            public MyViewHolder(View itemView) {
                super(itemView);
                quiz_title = itemView.findViewById(R.id.quiz_title_at_course_leading);
                course_name = itemView.findViewById(R.id.course_name_at_course_leading);
                total_questions = itemView.findViewById(R.id.total_questions_at_course_leading);
                cardview_content = itemView.findViewById(R.id.quiz_card_at_course_leading);
            }
        }
}