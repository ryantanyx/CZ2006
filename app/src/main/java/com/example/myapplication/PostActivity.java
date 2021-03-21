package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView backbutton;
    private Button postbutton;
    private EditText posttitle, postcontent;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root;


//Defining objects inside oncreate ----------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postactivity);

        backbutton =  (ImageView) findViewById(R.id.backbutton);
        backbutton.setOnClickListener(this);

        postbutton =  (Button) findViewById(R.id.postbutton);
        postbutton.setOnClickListener(this);

        posttitle = (EditText) findViewById(R.id.posttitle);
        postcontent = (EditText) findViewById(R.id.postcontent);


    }


//Button switch case -----------------------------
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backbutton:
                backbuttonmethod();
                break;
            case R.id.postbutton:
                AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
                builder.setTitle("WARNING")
                        .setMessage("Created Posts Cannot Be Deleted!")
                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                postbuttonmethod();

                            }
                        })
                        .setNegativeButton("Cancel", null);

                AlertDialog alert = builder.create();
                alert.show();
                break;

            default:
                break;
        }
    }


//Methods below-------------------------------

    public void backbuttonmethod(){
        PostActivity.this.finish();

    }

    public void postbuttonmethod(){
        if (posttitle.length()== 0)
        {
            posttitle.setError("Please enter a title");
        }
        else
        {
            String title = posttitle.getText().toString();
            String content = postcontent.getText().toString();
            Post post = new Post(title, content);
            root = db.getReference("Posts").push();
            String postKey = root.getKey();
            post.setPostKey(postKey);

            root.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    PostActivity.this.finish();
                }
            });

        }

    }



}