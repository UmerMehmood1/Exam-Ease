package com.picsart.studio.Student.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.picsart.studio.Student.Activities.Student_Course_Enrolled_Activity;
import com.picsart.studio.Models.Course;
import com.picsart.studio.R;

import java.util.List;

;

public class Course_Enrolled_Adapter extends RecyclerView.Adapter<Course_Enrolled_Adapter.MyViewHolder> {

    private Context context ;
    public List<Course> mData ;


    public Course_Enrolled_Adapter(Context context, List<Course> mData) {
        this.context = context;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_cardview_course,parent,false);
        return new MyViewHolder(view);
    }
    public void setData(List<Course> enrolled_courses) {
        this.mData.clear(); // Clear the existing data
        this.mData.addAll(enrolled_courses); // Add the new data
        notifyDataSetChanged(); // Notify the adapter of the data change
    }

    @Override
    public void onBindViewHolder(@NonNull Course_Enrolled_Adapter.MyViewHolder holder, int position) {
        holder.course_img.setImageResource(mData.get(position).getImg());
        holder.course_title.setText(mData.get(position).getName());
        holder.category.setText(mData.get(position).getCategory());
        holder.duration.setText(mData.get(position).getDuration());
        holder.t_quiz.setText(String.valueOf(mData.get(position).getTotalQuizzes()));
        holder.cardview_content.setOnClickListener(l ->{
            Intent intent = new Intent(context, Student_Course_Enrolled_Activity.class);
            intent.putExtra("Content_type_img",mData.get(position).getImg());
            intent.putExtra("content_name",mData.get(position).getName());
            intent.putExtra("content_description",mData.get(position).getDescription());
            intent.putExtra("course_id",mData.get(position).getId());
            context.startActivity(intent);
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

        ImageView course_img;
        TextView course_title, category, duration, t_quiz;
        CardView cardview_content;

        public MyViewHolder(View itemView) {
            super(itemView);

            course_title = itemView.findViewById(R.id.course_title_at_enrolled) ;
            category = itemView.findViewById(R.id.course_category_at_enrolled);
            duration = itemView.findViewById(R.id.course_duration_at_enrolled);
            t_quiz = itemView.findViewById(R.id.total_quizzes_at_enrolled);
            course_img = itemView.findViewById(R.id.course_img);
            cardview_content = itemView.findViewById(R.id.cardview_course_on_home);
        }
    }


}