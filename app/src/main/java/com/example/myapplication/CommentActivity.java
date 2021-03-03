package com.example.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class CommentActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView CAposttitle, CApostcontent, username;
    private ImageView CAbackbutton;
    private EditText edittextpostcomment;
    private Button CApostcommentbutton;
    private String postKey, content, title;

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




                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (comment.getUsername().equals(name))
                        {
                            deleteButtonmethod(cid);
                            Snackbar.make(getCurrentFocus(), "Comment has been deleted!", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Dismiss", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    }).show();
                        }
                        else
                        {
                            System.out.println("Nope");
                            Snackbar.make(getCurrentFocus(), "Unable to delete comments of others!", Snackbar.LENGTH_INDEFINITE)
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
            case R.id.CApostcommentbutton:
                CApostcommentbuttonmethod();
                break;

            default:
                break;
        }

    }

    public void CAbackbuttonmethod(){
        CommentActivity.this.finish();

    }

    public void CApostcommentbuttonmethod(){


        if (edittextpostcomment.length()== 0)
        {
            edittextpostcomment.setError("Comment cannot be empty!");
            edittextpostcomment.setText("");
        }


        else
        {

            DatabaseReference root = db.getReference("Comment").child(postKey).push();
            String comment_content = edittextpostcomment.getText().toString();
            Comment comment = new Comment(comment_content, name, imageNo);

            //create id for the comment
            String cid = root.getKey();
            comment.setCid(cid);

            root.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(CommentActivity.this, "Comment has been added", Toast.LENGTH_SHORT).show();
                    edittextpostcomment.setText("");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CommentActivity.this, "Failed to add comment", Toast.LENGTH_SHORT).show();
                    edittextpostcomment.setText("");
                }
            });

        }


    }



    public void deleteButtonmethod(String cid){
        DatabaseReference root = db.getReference("Comment").child(postKey).child(cid);
        root.removeValue();


    }




}