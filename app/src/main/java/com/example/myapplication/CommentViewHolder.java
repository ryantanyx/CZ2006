package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentViewHolder extends RecyclerView.ViewHolder {

    TextView usercomment;
    View view;


    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);

        usercomment = itemView.findViewById(R.id.usercomment);
        view = itemView;
    }
}
