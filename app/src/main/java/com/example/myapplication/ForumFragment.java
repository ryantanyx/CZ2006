package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForumFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Posts");
    private FirebaseRecyclerOptions<Post> options;
    private FirebaseRecyclerAdapter<Post, PostViewHolder> adapter;
    private RecyclerView recyclerView;
    private ImageView createPost;


    public ForumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForumFragment newInstance(String param1, String param2) {
        ForumFragment fragment = new ForumFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        getActivity().setTitle("Forum");


//Initialise buttons---------------------------------------------------


        createPost = (ImageView) view.findViewById(R.id.createPost);
        createPost.setOnClickListener(this);


//Initialise and run recyclerview and adapter-----------------------------------------------
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        options = new FirebaseRecyclerOptions.Builder<Post>().setQuery(root, Post.class).build();
        adapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Post post) {

                final String title = post.getTitle();
                final String content = post.getContent();
                final String postKey = post.getPostKey();
                final String username = post.getUsername();
                final String email = post.getEmail();

                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), CommentActivity.class);
                        intent.putExtra("title", "" + title);
                        intent.putExtra("content", content);
                        intent.putExtra("username", username);
                        intent.putExtra("postKey", postKey);
                        intent.putExtra("email", email);
                        startActivity(intent);

                    }
                });

                if (post.getTitle().length()>60)
                {
                    holder.itemtitle.setText("" + post.getTitle().substring(0,60) + "...");
                }
                else
                {
                    holder.itemtitle.setText("" + post.getTitle());
                }

                if (post.getContent().length()>100)
                {
                    holder.itemcontent.setText(post.getContent().substring(0,100) + "...");
                }
                else
                {
                    holder.itemcontent.setText(post.getContent());
                }

                holder.itemusername.setText(post.getUsername());



            }

            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = getLayoutInflater().from(parent.getContext()).inflate(R.layout.post_single_view, parent, false);
                return new PostViewHolder(v);
            }
        };


        adapter.startListening();
        recyclerView.setAdapter(adapter);

        return view;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createPost:
                startActivity(new Intent(getActivity(), PostActivity.class));
                break;
            default:
                break;
        }
    }




}