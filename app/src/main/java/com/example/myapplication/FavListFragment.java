package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
import java.util.Arrays;
import java.util.List;
import java.util.zip.Inflater;

public class FavListFragment extends Fragment{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView recyclerView;
    FavListAdapter adapter;
    ArrayList<School> items;
    Button btn;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private String mParam1;
    private String mParam2;
    private Dialog dialog;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private User userProfile;
    private ArrayList<School> favlist;
    public FavListFragment() {
        // Required empty public constructor
    }

    public static FavListFragment newInstance(String param1, String param2) {
        FavListFragment fragment = new FavListFragment();
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_favlist, container, false);
        getActivity().setTitle("Favourite List");











        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        // getting firebase reference
        reference.child(userID).child("favList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favlist = new ArrayList<School>();
                for (DataSnapshot snapchild: snapshot.getChildren()) {
                    School sch = snapchild.getValue(School.class);
                    favlist.add(sch);
                }
                recyclerView = view.findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                if (favlist != null){
                    adapter = new FavListAdapter(getActivity(), favlist);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(view.getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });

        btn = view.findViewById(R.id.compareSelector);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                String[] schools = new String[favlist.size()];
                boolean[] checkedSchs = new boolean[favlist.size()];

                for (int i = 0; i < favlist.size(); i++){
                    schools[i] = favlist.get(i).getSchoolName();
                    checkedSchs[i] = false;
                }
                List<String> schList = Arrays.asList(schools);
                builder.setMultiChoiceItems(schools, checkedSchs, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checkedSchs[which] = isChecked;
                    }
                });
                builder.setCancelable(false);

                // Set a title for alert dialog
                builder.setTitle("Select your schools for comparison");

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int count = 0;
                        for (int k = 0; k< checkedSchs.length; k++){
                            if (checkedSchs[k]) count++;
                        }
                        if (count == 2){
                            //Supposed to bring up another page
                            ArrayList<School> selectedSchs = new ArrayList<School>();
                            for (int j = 0; j< checkedSchs.length; j++){
                                boolean checked = checkedSchs[j];
                                if (checked) {
                                    for (School sch : favlist){
                                        if (schList.get(j).equals(sch.getSchoolName())){
                                            selectedSchs.add(sch);
                                        }
                                    }
                                }
                            }
                            Intent i = new Intent(v.getContext(), Comparison.class);
                            i.putExtra("Selected", selectedSchs);
                            v.getContext().startActivity(i);
                        } else {
                            Toast.makeText(view.getContext(), "Choose exactly 2 schools!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                // Set the negative/no button click listener
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when click the negative button
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                // Display the alert dialog on interface
                dialog.show();
            }
        });

        return view;
    }
}
