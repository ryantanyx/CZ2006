package com.example.myapplication;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentViewHolder extends RecyclerView.ViewHolder {

    TextView usercomment, username;
    ImageView userImage, deletecommentbutton;
    View view;

    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);

        CommentActivity commentActivity = new CommentActivity();

        deletecommentbutton = itemView.findViewById(R.id.deletecommentbutton);
        usercomment = itemView.findViewById(R.id.usercomment);
        username = itemView.findViewById(R.id.username);
        userImage = itemView.findViewById(R.id.userImage);
        view = itemView;
    }
}
