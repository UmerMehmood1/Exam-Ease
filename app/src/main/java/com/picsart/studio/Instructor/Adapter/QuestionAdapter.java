package com.picsart.studio.Instructor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.picsart.studio.Models.Question;
import com.picsart.studio.R;
import java.util.List;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.RadioGroup;
import android.widget.Toast;


public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.MyViewHolder> {

    private Context context;
    private String course_id;
    private String teacher_id;
    private List<Question> questionList;

    public QuestionAdapter(Context context, String teacher_id, String course_id, List<Question> questionList) {
        this.context = context;
        this.course_id = course_id;
        this.teacher_id = teacher_id;
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.instructor_add_question_cardview, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Question question = questionList.get(position);
        Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
        holder.question_content.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                question.setQuestionText(s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        holder.option_1_content.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                question.getOptions().set(0, s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        holder.option_2_content.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                question.getOptions().set(1, s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        holder.option_3_content.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                question.getOptions().set(2, s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        holder.option_4_content.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                question.getOptions().set(3, s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        holder.option_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    question.setSelectedOption(0);
                }
            }
        });
        holder.option_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    question.setSelectedOption(1);
                }
            }
        });
        holder.option_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    question.setSelectedOption(2);
                }
            }
        });
        holder.option_4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    question.setSelectedOption(3);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        EditText question_content, option_1_content, option_2_content, option_3_content, option_4_content;
        RadioButton option_1, option_2, option_3, option_4;
        RadioGroup radioGroup;
        CardView cardview_content;

        public MyViewHolder(View itemView) {
            super(itemView);
            question_content = itemView.findViewById(R.id.editTextQuestionText);
            option_1_content = itemView.findViewById(R.id.editTextOption1);
            option_2_content = itemView.findViewById(R.id.editTextOption2);
            option_3_content = itemView.findViewById(R.id.editTextOption3);
            option_4_content = itemView.findViewById(R.id.editTextOption4);
            option_1 = itemView.findViewById(R.id.radioButtonOption1);
            option_2 = itemView.findViewById(R.id.radioButtonOption2);
            option_3 = itemView.findViewById(R.id.radioButtonOption3);
            option_4 = itemView.findViewById(R.id.radioButtonOption4);
            radioGroup = itemView.findViewById(R.id.radioGroupOptions);
            cardview_content = itemView.findViewById(R.id.question_card);
        }
    }

    public List<Question> getQuestionList(){
        return questionList;
    }
}
