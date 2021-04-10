package com.example.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
/**
 * Represents the Post Activity Boundary whereby a user creates a post
 */
public class PostActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * ImageView to display back button
     */
    private ImageView backbutton;
    /**
     * Button to create post
     */
    private Button postbutton;
    /**
     * EditText for users to enter post title and content
     */
    private EditText posttitle, postcontent;
    /**
     * Instance of the Firebase
     */
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    /**
     * Reference in database to retrieve information from
     */
    private DatabaseReference root;
    /**
     * User stored in Firebase
     */
    private FirebaseUser user;
    /**
     * Reference in database to retrieve information from
     */
    private DatabaseReference reference;
    /**
     * User ID stored in Firebase
     */
    private String userID;
    /**
     * The name of the user who created the post
     */
    private String name;
    /**
     * Email of the user who created the post
     */
    private String email;
    /**
     * Creation of activity from savedInstanceState and setting the layout
     * @param savedInstanceState
     */
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
    /**
     * Switch case to execute different commands for the respective buttons
     * @param v View
     */
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
                                if (!postsuccess) {
                                    posttitle.setError("Please enter a title");
                                }
                                else {
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
    /**
     * Stops activity when back button is pressed
     */
    public void backbuttonmethod(){
        PostActivity.this.finish();
    }
}