package com.picsart.studio.Student.Adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.picsart.studio.Student.Activities.Course_Detail;
import com.picsart.studio.Models.Course;
import com.picsart.studio.R;

import java.util.ArrayList;
import java.util.List;
public class Available_Course_Adapter extends RecyclerView.Adapter<Available_Course_Adapter.MyViewHolder> {
    private Context context ;
    private List<Course> mData ;
    public Available_Course_Adapter(Context context, List<Course> mData) {
        this.context = context;
        this.mData = mData;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_cardview_course,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.course_title.setText(mData.get(position).getName());
        holder.category.setText(mData.get(position).getCategory());
        holder.t_quiz.setText(String.valueOf(mData.get(position).getTotalQuizzes()));
        holder.duration.setText(mData.get(position).getDuration());
        holder.course_img.setImageResource(mData.get(position).getImg());
        holder.cardview_content.setOnClickListener(l ->{
                Intent intent = new Intent(context, Course_Detail.class);
                // passing data to the book activity
                intent.putExtra("Course_id",mData.get(position).getId());
                intent.putExtra("Title",mData.get(position).getName());
                intent.putExtra("Description",mData.get(position).getDescription());
                intent.putExtra("Category",mData.get(position).getCategory());
                intent.putExtra("Thumbnail",mData.get(position).getImg());
                context.startActivity(intent);
        });


    }
    public void setData(List<Course> enrolled_courses) {
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