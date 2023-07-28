package com.picsart.studio.Instructor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.picsart.studio.Instructor.InstructorFragments.CourseQuizzes;
import com.picsart.studio.Instructor.CustomDialogBox.UpdateCourseDialogBox;
import com.picsart.studio.Models.Course;
import com.picsart.studio.R;
import java.util.List;


public class Instructor_Course_Leading_Adapter extends RecyclerView.Adapter<Instructor_Course_Leading_Adapter.MyViewHolder> {

    private Context context ;
    private List<Course> mData ;
    private String teacher_id ;


    public Instructor_Course_Leading_Adapter(Context context, List<Course> mData, String teacher_id) {
        this.context = context;
        this.mData = mData;
        this.teacher_id = teacher_id;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.instructor_course_leading_cardview,parent,false);
        return new MyViewHolder(view);
    }

    public void setData(List<Course> enrolled_courses) {
        this.mData.clear(); // Clear the existing data
        this.mData.addAll(enrolled_courses); // Add the new data
        notifyDataSetChanged(); // Notify the adapter of the data change
    }
    @Override
    public void onBindViewHolder(@NonNull Instructor_Course_Leading_Adapter.MyViewHolder holder, int position) {

        holder.course_img.setImageResource(mData.get(position).getImg());
        holder.course_title.setText(mData.get(position).getName());
        holder.course_category.setText(mData.get(position).getCategory());
        holder.course_duration.setText(mData.get(position).getDuration());
        holder.course_quizzes.setText(String.valueOf(mData.get(position).getTotalQuizzes()));
        holder.cardview_content.setOnClickListener(l ->{
            SharedPreferences sh = context.getSharedPreferences("course_data", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sh.edit();
            editor.putString("course_id",mData.get(position).getId());
            editor.putString("course_name",mData.get(position).getName());
            editor.putInt("course_img",mData.get(position).getImg());
            editor.putString("course_category",mData.get(position).getCategory());
            editor.putString("course_description",mData.get(position).getDescription());
            editor.putString("course_duration",mData.get(position).getDuration());
            editor.putString("course_id",mData.get(position).getId());
            context.startActivity(new Intent(context, CourseQuizzes.class));
        });
        holder.cardview_content.setOnLongClickListener(l->{
            UpdateCourseDialogBox updateCourseDialogBox = new UpdateCourseDialogBox(context, mData.get(position).getId(),teacher_id);
            updateCourseDialogBox.show();
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

        ImageView course_img;
        TextView course_title, course_category, course_duration, course_quizzes;
        CardView cardview_content;

        public MyViewHolder(View itemView) {
            super(itemView);


            course_img = itemView.findViewById(R.id.course_img_at_leading) ;
            course_title = itemView.findViewById(R.id.course_title_at_leading);
            course_category = itemView.findViewById(R.id.course_category_at_leading);
            course_duration = itemView.findViewById(R.id.course_duration_at_leading);
            course_quizzes = itemView.findViewById(R.id.course_quizzes_at_leading);
            cardview_content = itemView.findViewById(R.id.cardview_course_at_course_leading);


        }
    }


}
