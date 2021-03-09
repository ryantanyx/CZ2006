package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements Filterable{

    private LayoutInflater layoutInflater;
    private List<School> data;
    private List<School> dataset;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    Adapter(Context context, List<School> data){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.dataset = new ArrayList<School>(data);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.custom_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        School school = data.get(position);
        String imageUrl = school.getImageUrl();
        String schoolName = school.getSchoolName();
        String schoolAddress = school.getAddress();
        Glide.with(holder.schoolImage.getContext()).load(imageUrl).error(R.drawable.ic_person).into(holder.schoolImage);
        holder.schoolTitle.setText(schoolName);
        holder.schoolDesc.setText(schoolAddress);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<School> filteredList = new ArrayList<>();

            if(constraint.toString().isEmpty()){
                filteredList.addAll(dataset);
            }
            else{
                for (School school: dataset){
                    if (school.getSchoolName().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredList.add(school);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            data.clear();
            data.addAll((Collection<? extends School>) results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView schoolImage;
        TextView schoolTitle, schoolDesc;
        ImageButton favIcon;
        Boolean flag = true;
        ArrayList<School> favlist;
        User userProfile;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            schoolImage = itemView.findViewById(R.id.schoolImage);
            schoolTitle = itemView.findViewById(R.id.schoolTitle);
            schoolDesc = itemView.findViewById(R.id.schoolDesc);
            favIcon = itemView.findViewById(R.id.starIcon);
            favIcon.setImageResource(R.drawable.ic_normalstar);

            user = FirebaseAuth.getInstance().getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference("Users");
            userID = user.getUid();

            // getting firebase reference
            reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userProfile = snapshot.getValue(User.class);

                    if (userProfile != null){
                        favlist = userProfile.getFavList();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(itemView.getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                }
            });
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(v.getContext(), Details.class);
                    i.putExtra("School", data.get(getAdapterPosition()));
                    v.getContext().startActivity(i);
                }
            });

            favIcon.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //favIcon.setSelected(!favIcon.isPressed());
                    if (flag) {
                        addSchoolToFav(favlist);       //Remove comment to test
                        favIcon.setImageResource(R.drawable.ic_favstar);
                        Toast.makeText(v.getContext(), "School has been added to favourite list", Toast.LENGTH_SHORT).show();
                        flag = false;
                    }
                    else {
                        removeSchoolfromFav(favlist);      //Remove comment to test
                        favIcon.setImageResource(R.drawable.ic_normalstar);
                        Toast.makeText(v.getContext(), "School has been removed from favourite list", Toast.LENGTH_SHORT).show();
                        flag = true;
                    }
                }

                private void removeSchoolfromFav(ArrayList<School> favlist) {
                    School school =data.get(getAdapterPosition());
                    /*if (userProfile.getFavList() == null) {
                        current_fav = new ArrayList<School>();
                    }*/
                    favlist.remove(school);
                    userProfile.setFavList(favlist);
                    reference.child(userID).child("favList").setValue(favlist);
                    System.out.println(favlist.toString());
                }

                private void addSchoolToFav(ArrayList<School> favlist) {
                    School school =data.get(getAdapterPosition());
                    if (favlist == null) {
                        favlist = new ArrayList<School>();
                    }
                    favlist.add(school);
                    userProfile.setFavList(favlist);
                    reference.child(userID).child("favList").setValue(favlist);
                    System.out.println(favlist.toString());
                }
            });
        }
    }
}
