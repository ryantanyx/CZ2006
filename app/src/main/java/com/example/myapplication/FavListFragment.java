package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class FavListFragment extends Fragment {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    
    RecyclerView recyclerView;
    FavListAdapter adapter;
    ArrayList<School> items;
    
    private String mParam1;
    private String mParam2;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private User userProfile;
    private ArrayList<School> favlist;
    public FavListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        // getting firebase reference
        reference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userProfile = snapshot.getValue(User.class);
                if (userProfile != null){
                    ArrayList<School> favlist = new ArrayList<School>();
                    for (DataSnapshot snapchild: snapshot.child("favList").getChildren()) {
                        School sch = snapchild.getValue(School.class);
                        favlist.add(sch);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(recyclerView.getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_favlist, container, false);
        getActivity().setTitle("Favourite List");

        recyclerView = (RecyclerView) view.findViewById(R.id.favListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new FavListAdapter(getActivity(), favlist);
        recyclerView.setAdapter(adapter);

        return view;
    }


}
