package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavListAdapter extends RecyclerView.Adapter<FavListAdapter.ViewHolder>{
    private final LayoutInflater layoutInflater;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private User userProfile;
    private List<School> favlist;

    FavListAdapter(Context context, List<School> favlist){
        this.layoutInflater = LayoutInflater.from(context);
        this.favlist = favlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_view, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        School school = favlist.get(position);
        String schoolName = school.getSchoolName();
        holder.schoolTitle.setText(schoolName);
    }


    @Override
    public int getItemCount() {
        return favlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView schoolImage;
        TextView schoolTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            schoolImage = itemView.findViewById(R.id.schoolImage);
            schoolTitle = itemView.findViewById(R.id.schoolTitle);
        }
    }
}
