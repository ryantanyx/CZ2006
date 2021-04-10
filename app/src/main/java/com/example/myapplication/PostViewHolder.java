package com.example.myapplication;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Represents the Post ViewHolder Controller which controls the layout of the posts created using RecyclerView
 */
public class PostViewHolder extends RecyclerView.ViewHolder {

    TextView itemcontent, itemtitle, itemusername;
    EditText edittextpostcomment;
    View view;

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);

        itemtitle = itemView.findViewById(R.id.itemtitle);
        itemcontent = itemView.findViewById(R.id.itemcontent);
        itemusername = itemView.findViewById(R.id.itemusername);
        edittextpostcomment = itemView.findViewById(R.id.edittextpostcomment);
        view = itemView;
    }
}