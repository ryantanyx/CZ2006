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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView backbutton;
    private Button postbutton;
    private EditText posttitle, postcontent;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private String name;
    private String email;
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

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        //Find the user creating the post
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {

                    name = userProfile.getName();
                    email = user.getEmail();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PostActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });

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
                        .setMessage("Your posts will be viewable by anyone using this application. Are you sure you wish to continue?")
                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                boolean postsuccess = ForumController.postbuttonmethod(posttitle, postcontent, name, email);


                                if (!postsuccess)
                                {
                                    posttitle.setError("Please enter a title");
                                }
                                else
                                {
                                    PostActivity.this.finish();
                                }

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





}