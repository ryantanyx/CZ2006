package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Favourite List Adapter Controller which controls the favourite list functions
 */
public class FavListAdapter extends RecyclerView.Adapter<FavListAdapter.ViewHolder>{
    /**
     * Layout inflater to instantiate layout from XML file
     */
    private final LayoutInflater layoutInflater;
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
     * List of schools stored in favourite list
     */
    private List<School> favlist;

    /**
     * Constructor to create a new favourite list adapter
     * @param context The application's current context
     * @param favlist The list of schools stored in favourite list
     */
    FavListAdapter(Context context, List<School> favlist){
        this.layoutInflater = LayoutInflater.from(context);
        this.favlist = favlist;
    }

    /**
     * Creates a view holder in the parent ViewGroup with the specified viewType
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_view, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Displays the view at the specified position
     * @param holder The view holder whose contents should be updated
     * @param position The position of the holder with respect to this adapter
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        School school = favlist.get(position);
        String imageUrl = school.getImageUrl();
        String schoolName = school.getSchoolName();
        String schoolAddress = school.getAddress();
        Glide.with(holder.schoolImage.getContext()).load(imageUrl).error(R.drawable.ic_person).into(holder.schoolImage);
        holder.schoolTitle.setText(schoolName);
        holder.schoolDesc.setText(schoolAddress);
        holder.favIcon.setImageResource(R.drawable.ic_favstar);

    }

    /**
     * Get the size of the favourite list
     * @return integer to indicate size of favourite list
     */
    @Override
    public int getItemCount() {
        return favlist.size();
    }

    /**
     * ViewHolder class to describe an item view and metadata about its place within the RecyclerView
     */
    public class ViewHolder extends RecyclerView.ViewHolder{
        /**
         * ImageView for the school logo
         */
        ImageView schoolImage;
        /**
         * TextView for the school title and description
         */
        TextView schoolTitle, schoolDesc;
        /**
         * ImageButton to display a clickable favourite icon
         */
        ImageButton favIcon;
        boolean success;

        /**
         * Constructor to create a new ViewHolder
         * @param itemView
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            schoolImage = itemView.findViewById(R.id.schoolImage);
            schoolTitle = itemView.findViewById(R.id.schoolTitle);
            schoolDesc = itemView.findViewById(R.id.schoolDesc);
            favIcon = itemView.findViewById(R.id.starIcon);
            favIcon.setTag(R.drawable.ic_favstar);

            user = FirebaseAuth.getInstance().getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference("Users");
            userID = user.getUid();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(v.getContext(), Details.class);
                    School school = favlist.get(getAdapterPosition());
                    i.putExtra("School", school);
                    v.getContext().startActivity(i);
                }
            });

            reference.child(userID).addChildEventListener(new ChildEventListener() {

                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    return;
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    favlist = new ArrayList<School>();
                    for (DataSnapshot snapchild: snapshot.getChildren()) {
                        School sch = snapchild.getValue(School.class);
                        favlist.add(sch);
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    return;
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    return;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    return;
                }

            });

            favIcon.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    School school = favlist.get(getAdapterPosition());
                    if (Integer.parseInt(favIcon.getTag().toString()) == R.drawable.ic_favstar){
                        success = removeSchoolfromFav(favlist, school);
                        if (success) {
                            favIcon.setImageResource(R.drawable.ic_normalstar);
                            favIcon.setTag(R.drawable.ic_normalstar);
                            Toast.makeText(v.getContext(), "School has been removed from favourite list", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(v.getContext(), "School is not in favourite list", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                private Boolean removeSchoolfromFav(List<School> favlist, School school) {
                    for (School sch : favlist) {
                        if (sch.getSchoolName().equals(school.getSchoolName())) {
                            favlist.remove(sch);
                            reference.child(userID).child("favList").setValue(favlist);
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }
}
