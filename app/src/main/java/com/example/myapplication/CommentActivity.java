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

public class CommentActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView CAposttitle, CApostcontent;
    private ImageView CAbackbutton, deletepostbutton;
    private EditText edittextpostcomment;
    private Button CApostcommentbutton;
    private ImageView deletecommentbutton;
    private String postKey, content, title, postUsername;
    private FirebaseRecyclerOptions<Comment> options;
    private FirebaseRecyclerAdapter<Comment, CommentViewHolder> adapter;
    private RecyclerView recyclerView;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private int imageNo;
    private String name;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Comment");



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
                            ForumController.deleteButtonmethod(cid, postKey);
                            Snackbar.make(findViewById(android.R.id.content), "Comment deleted!", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Dismiss", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    }).show();

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

                 if (createpostinteger == 0)
                 {
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
                     Snackbar.make(findViewById(android.R.id.content), "Comment cannot exceed 30 characters!", Snackbar.LENGTH_INDEFINITE)
                             .setAction("Dismiss", new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {

                                 }
                             }).show();
                     edittextpostcomment.setText("");
                 }
                 else
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

    public void CAbackbuttonmethod(){
        CommentActivity.this.finish();

    }




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