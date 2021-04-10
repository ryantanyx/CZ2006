package com.example.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Represents the Comment Activity Boundary whereby a user comments on a post
 */
public class CommentActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * TextView to display post title and post content
     */
    private TextView CAposttitle, CApostcontent;
    /**
     * ImageView to display back button and delete post icon
     */
    private ImageView CAbackbutton, deletepostbutton;
    /**
     * EditText for user to type his/her comment
     */
    private EditText edittextpostcomment;
    /**
     * Button to post comment
     */
    private Button CApostcommentbutton;
    /**
     * ImageView to display delete comment icon
     */
    private ImageView deletecommentbutton;
    /**
     * Strings to store key, content, title of post, and name of user who created the post
     */
    private String postKey, content, title, postUsername;
    /**
     * FirebaseOptions to configure the firebase
     */
    private FirebaseRecyclerOptions<Comment> options;
    /**
     * Firebase adapter that responds to changes in the firebase
     */
    private FirebaseRecyclerAdapter<Comment, CommentViewHolder> adapter;
    /**
     * RecyclerView to contain the comment
     */
    private RecyclerView recyclerView;
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
     * Stores user's profile image number to display the correct image
     */
    private int imageNo;
    /**
     * The name of the user who made the comment
     */
    private String name;
    /**
     * Email of the user who created the post
     */
    private String postEmail;
    /**
     * Instance of FirebaseDatabase
     */
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    /**
     * Reference to the "Comment" child in Firebase
     */
    private DatabaseReference root = db.getReference().child("Comment");


    /**
     * Creation of activity from savedInstanceState and setting the layout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        CAposttitle = findViewById(R.id.CAposttitle);
        CApostcontent = findViewById(R.id.CApostcontent);

        CAbackbutton = (ImageView) findViewById(R.id.CAbackbutton);
        CAbackbutton.setOnClickListener(this);


        deletepostbutton = (ImageView) findViewById(R.id.deletepostbuttton);
        deletepostbutton.setOnClickListener(this);


        edittextpostcomment = (EditText) findViewById(R.id.edittextpostcomment);

        CApostcommentbutton = (Button) findViewById(R.id.CApostcommentbutton);
        CApostcommentbutton.setOnClickListener(this);


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();


        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {

                    imageNo = userProfile.getImageNo();
                    name = userProfile.getName();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CommentActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });


        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            title = extras.getString("title");
            content = extras.getString("content");
            postKey = extras.getString("postKey");
            postUsername = extras.getString("username");
            postEmail = extras.getString("email");

            CAposttitle.setText(title);
            CApostcontent.setText(content);
        }

//Initialise and run recyclerview and adapter-----------------------------------------------
        recyclerView = findViewById(R.id.commentRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        options = new FirebaseRecyclerOptions.Builder<Comment>().setQuery(root.child(postKey), Comment.class).build();
        adapter = new FirebaseRecyclerAdapter<Comment, CommentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CommentViewHolder holder, int position, @NonNull Comment comment) {

                final String cid = comment.getCid();

                holder.username.setText(comment.getUsername());
                holder.usercomment.setText(comment.getUsercomment());
                if (comment.getImageNo() == 1){
                    holder.userImage.setImageResource(R.drawable.image1);
                }
                else if (comment.getImageNo() == 2){
                    holder.userImage.setImageResource(R.drawable.image2);
                }
                else if (comment.getImageNo() == 3){
                    holder.userImage.setImageResource(R.drawable.image3);
                }
                else if (comment.getImageNo() == 4){
                    holder.userImage.setImageResource(R.drawable.image4);
                }


                holder.deletecommentbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean checkcomment = ForumController.checkComment(comment, name);

                        if (checkcomment)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(CommentActivity.this);
                            builder.setTitle("WARNING")
                                    .setMessage("Are you sure you wish to delete your comment?")
                                    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            ForumController.deleteButtonmethod(cid, postKey);
                                            Snackbar.make(findViewById(android.R.id.content), "Comment deleted!", Snackbar.LENGTH_INDEFINITE)
                                                    .setAction("Dismiss", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                        }
                                                    }).show();


                                        }
                                    })
                                    .setNegativeButton("Cancel", null);
                            AlertDialog alert = builder.create();
                            alert.show();


                        }
                        else
                        {
                            Snackbar.make(findViewById(android.R.id.content), "You cannot delete the comments of others!", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Dismiss", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    }).show();
                        }
                    }
                });


            }


            @NonNull
            @Override
            public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = getLayoutInflater().from(parent.getContext()).inflate(R.layout.single_view_comment, parent, false);
                return new CommentViewHolder(v);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    /**
     * Switch case to execute different commands for the respective buttons
     * @param v View
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.CAbackbutton:
                CAbackbuttonmethod();
                break;

            case R.id.deletepostbuttton:
                boolean checkpost = ForumController.checkPost(postUsername, name);

                if (checkpost)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CommentActivity.this);
                    builder.setTitle("WARNING")
                            .setMessage("Your post and all comments will be deleted. Are you sure you wish to continue?")
                            .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    ForumController.deletepostmethod(postKey);
                                    CommentActivity.this.finish();

                                }
                            })
                            .setNegativeButton("Cancel", null);
                    AlertDialog alert = builder.create();
                    alert.show();
                    break;

                }

                else
                {
                    Snackbar.make(findViewById(android.R.id.content), "You cannot delete the posts of others!", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).show();
                }
                break;


            case R.id.CApostcommentbutton:
                int createpostinteger = ForumController.CApostcommentbuttonmethod(edittextpostcomment, postKey, name, imageNo);
                String emailContent = name + " commented on your post '" + title + "' : '" + edittextpostcomment.getText().toString() + "'";

                 if (createpostinteger == 0)
                 {
                     JavaMailAPI javaMailAPI = new JavaMailAPI(CommentActivity.this, postEmail, "New Comment", emailContent);
                     javaMailAPI.execute();
                     Snackbar.make(findViewById(android.R.id.content), "Comment added!", Snackbar.LENGTH_INDEFINITE)
                             .setAction("Dismiss", new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {

                                 }
                             }).show();
                     edittextpostcomment.setText("");

                 }
                 else if (createpostinteger ==1)
                 {
                     Snackbar.make(findViewById(android.R.id.content), "Comment cannot exceed 100 characters!", Snackbar.LENGTH_INDEFINITE)
                             .setAction("Dismiss", new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {

                                 }
                             }).show();
                     edittextpostcomment.setText("");
                 }
                 else if (createpostinteger == 2)
                 {
                     Snackbar.make(findViewById(android.R.id.content), "Comment must have at least 5 characters!", Snackbar.LENGTH_INDEFINITE)
                             .setAction("Dismiss", new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {

                                 }
                             }).show();
                     edittextpostcomment.setText("");
                 }
                break;

            default:
                break;
        }

    }

    /**
     * Stops activity when back button is pressed
     */
    public void CAbackbuttonmethod(){
        CommentActivity.this.finish();

    }


    /**
     * Posts the comment  and updates Firebase if comment is valid
     * @param edittextpostcomment Comment written by user
     * @return integer to indicate validity of the comment written
     */
    public int CApostcommentbuttonmethod(EditText edittextpostcomment){


        if (edittextpostcomment.length()== 0)
        {
            return 1;
        }
        else if (edittextpostcomment.length()>30)
        {
            return 2;
        }

        else
        {

            DatabaseReference root = db.getReference("Comment").child(postKey).push();
            String comment_content = edittextpostcomment.getText().toString();
            Comment comment = new Comment(comment_content, name, imageNo);

            //create id for the comment
            String cid = root.getKey();
            comment.setCid(cid);
            root.setValue(comment);
            return 3;


        }


    }








}