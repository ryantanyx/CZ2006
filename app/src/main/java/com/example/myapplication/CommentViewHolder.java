package com.example.myapplication;

import android.content.DialogInterface;
import android.media.Image;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
