package com.example.myapplication;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
/**
 * Represents the CommentViewHolder Controller which controls the layout of the comments made using RecyclerView
 */
public class CommentViewHolder extends RecyclerView.ViewHolder {
    /**
     * TextView to display user's comment and name of user
     */
    TextView usercomment, username;
    /**
     * ImageView to display user's profile picture and delete comment icon
     */
    ImageView userImage, deletecommentbutton;
    /**
     * View to display comment in
     */
    View view;
    /**
     * Constructor to create a comment with the specified layout
     * @param itemView
     */
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
