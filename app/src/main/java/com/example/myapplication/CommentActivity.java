package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener {


    TextView CAposttitle, CApostcontent;
    ImageView CAbackbutton;
    EditText edittextpostcomment;
    Button CApostcommentbutton;
    String postKey, content, title;

    FirebaseRecyclerOptions<Comment> options;
    FirebaseRecyclerAdapter<Comment, CommentViewHolder> adapter;
    RecyclerView recyclerView;



    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference root = db.getReference().child("Comment");


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


                holder.usercomment.setText(comment.getUsercomment());
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
            edittextpostcomment.setError("Please enter a title");
            edittextpostcomment.setText("");
        }


        else
        {
            DatabaseReference root = db.getReference("Comment").child(postKey).push();
            String comment_content = edittextpostcomment.getText().toString();
            Comment comment = new Comment(comment_content);
            root.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(com.example.myapplication.CommentActivity.this, "Comment has been added", Toast.LENGTH_SHORT).show();
                    edittextpostcomment.setText("");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(com.example.myapplication.CommentActivity.this, "Failed to add comment", Toast.LENGTH_SHORT).show();
                    edittextpostcomment.setText("");
                }
            });
        }


    }





}