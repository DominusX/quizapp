package com.themaster.quizzer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.viewHolder> {


    List<QuestionModel> list;

    public BookmarksAdapter(List<QuestionModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_item,
                parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.setData(list.get(position).getQuestion(),
                list.get(position).getAnswer(), position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {
        TextView question;
        TextView answer;
        ImageButton delete;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.question_bookmark);
            answer = itemView.findViewById(R.id.answer_bookmark);
            delete = itemView.findViewById(R.id.btn_delete);

        }

        private void setData(String answer, String question, int position) {
            this.question.setText(question);
            this.answer.setText(answer);

            delete.setOnClickListener(v -> {
                list.remove(position);
                notifyItemRemoved(position);
            });

        }
    }
}
