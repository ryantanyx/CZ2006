package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.renderscript.Sampler;
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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    private LayoutInflater layoutInflater;
    private List<School> data;
    private List<School> dataset;
    private String selectedRegion = "all";
    private String selectedStream = "all";
    private String currentSearchText ="";
    private String selectedCCA = "all";
    private int min = 0,max = 300;

    private User userProfile;
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
        ArrayList<School> favlist = new ArrayList<School>();
        // getting firebase reference
        reference.child(userID).child("favList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapchild: snapshot.getChildren()) {
                    School sch = snapchild.getValue(School.class);
                    favlist.add(sch);
                }
                for (School sch : favlist){
                    if (sch.getSchoolName().equals(school.getSchoolName())){
                        holder.favIcon.setImageResource(R.drawable.ic_favstar);
                        holder.favIcon.setTag(R.drawable.ic_favstar);
                        break;
                    } else {
                        holder.favIcon.setImageResource(R.drawable.ic_normalstar);
                        holder.favIcon.setTag(R.drawable.ic_normalstar);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(holder.itemView.getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

//    @Override
//    public Filter getFilter() {
//        return filter;
//    }
//
//    Filter filter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//
//            List<School> filteredList = new ArrayList<>();
//
//            if(constraint.toString().isEmpty()){
//                filteredList.addAll(dataset);
//            }
//            else{
//                for (School school: dataset){
//                    if (school.getSchoolName().toLowerCase().contains(constraint.toString().toLowerCase())){
//                        filteredList.add(school);
//                    }
//                }
//            }
//
//            FilterResults filterResults = new FilterResults();
//            filterResults.values = filteredList;
//
//            return filterResults;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            data.clear();
//            data.addAll((Collection<? extends School>) results.values);
//            notifyDataSetChanged();
//        }
//    };

    public void searchViewFilter(String newText){
        currentSearchText = newText;
        resetSchoolList();
        filter();
    }

    public void filterRegion(String status){
        resetRegion();
        selectedRegion = status;
        filter();
    }

    public void filterPSLE(int low, int high){
        resetSchoolList();
        min = low;
        max = high;
        filter();
    }

    public void filterStream(String status){
        resetStream();
        selectedStream = status;
        filter();
    }

    public void filterCCA(String status){
        resetCCA();
        selectedCCA = status;
        filter();
    }

    public void filter(){
        List<School> filteredList = new ArrayList<>();
        for(School school:dataset) {
            if (!selectedStream.equals("all")) {
                if (!school.getCutOffPoint().get(selectedStream).equals(0)) {
                    if (school.getCutOffPoint().get(selectedStream) >= min && school.getCutOffPoint().get(selectedStream) <= max) {
                        if (!selectedRegion.equals("all")) {
                            if (school.getRegion().toLowerCase().contains(selectedRegion)) {
                                if (!selectedCCA.equals("all")) {
                                    if (school.getCca()!=null) {
                                        for (Map.Entry<String,ArrayList<String>> temp : school.getCca().entrySet()) {
                                            ArrayList<String> strings = temp.getValue();
                                            if (strings.contains(selectedCCA)) {
                                                if (currentSearchText.equals(""))
                                                    filteredList.add(school);
                                                else {
                                                    if (school.getSchoolName().toLowerCase().contains(currentSearchText.toLowerCase()))
                                                        filteredList.add(school);
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (currentSearchText.equals(""))
                                        filteredList.add(school);
                                    else {
                                        if (school.getSchoolName().toLowerCase().contains(currentSearchText.toLowerCase()))
                                            filteredList.add(school);
                                    }
                                }
                            }
                        } else {
                            if (!selectedCCA.equals("all")) {
                                if (school.getCca()!=null) {
                                    for (Map.Entry<String,ArrayList<String>> temp : school.getCca().entrySet()) {
                                        ArrayList<String> strings = temp.getValue();
                                        if (strings.contains(selectedCCA)) {
                                            if (currentSearchText.equals(""))
                                                filteredList.add(school);
                                            else {
                                                if (school.getSchoolName().toLowerCase().contains(currentSearchText.toLowerCase()))
                                                    filteredList.add(school);
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (currentSearchText.equals(""))
                                    filteredList.add(school);
                                else {
                                    if (school.getSchoolName().toLowerCase().contains(currentSearchText.toLowerCase()))
                                        filteredList.add(school);
                                }
                            }
                        }
                    }
                }
            }
            else {
                if ((school.getCutOffPoint().get("express") >= min && school.getCutOffPoint().get("express") <= max)
                        || (school.getCutOffPoint().get("na") != 0 && school.getCutOffPoint().get("na") >= min
                        && school.getCutOffPoint().get("na") <= max) || (school.getCutOffPoint().get("nt") != 0
                        && school.getCutOffPoint().get("nt") >= min && school.getCutOffPoint().get("nt") <= max)) {
                    if (!selectedRegion.equals("all")) {
                        if (school.getRegion().toLowerCase().contains(selectedRegion)) {
                            if (!selectedCCA.equals("all")) {
                                if (school.getCca()!=null) {
                                    for (Map.Entry<String,ArrayList<String>> temp : school.getCca().entrySet()) {
                                        ArrayList<String> strings = temp.getValue();
                                        if (strings.contains(selectedCCA)) {
                                            if (currentSearchText.equals(""))
                                                filteredList.add(school);
                                            else {
                                                if (school.getSchoolName().toLowerCase().contains(currentSearchText.toLowerCase()))
                                                    filteredList.add(school);
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (currentSearchText.equals(""))
                                    filteredList.add(school);
                                else {
                                    if (school.getSchoolName().toLowerCase().contains(currentSearchText.toLowerCase()))
                                        filteredList.add(school);
                                }
                            }
                        }
                    } else {
                        if (!selectedCCA.equals("all")) {
                            if (school.getCca()!=null) {
                                for (Map.Entry<String,ArrayList<String>> temp : school.getCca().entrySet()) {
                                    ArrayList<String> strings = temp.getValue();
                                    if (strings.contains(selectedCCA)) {
                                        if (currentSearchText.equals(""))
                                            filteredList.add(school);
                                        else {
                                            if (school.getSchoolName().toLowerCase().contains(currentSearchText.toLowerCase()))
                                                filteredList.add(school);
                                        }
                                    }
                                }
                            }
                        } else {
                            if (currentSearchText.equals(""))
                                filteredList.add(school);
                            else {
                                if (school.getSchoolName().toLowerCase().contains(currentSearchText.toLowerCase()))
                                    filteredList.add(school);
                            }
                        }
                    }

                }
            }
        }
        data.clear();
        data.addAll(filteredList);
        notifyDataSetChanged();
    }

    private void resetSchoolList(){
        data.clear();
        data.addAll(dataset);
    }

    public void resetRegion(){
        selectedRegion = "all";
        resetSchoolList();
        notifyDataSetChanged();
    }

    public void resetStream(){
        selectedStream = "all";
        resetSchoolList();
        notifyDataSetChanged();
    }

    public void resetCCA(){
        selectedCCA = "all";
        resetSchoolList();
        notifyDataSetChanged();
    }

    public void resetFilter(){
        selectedRegion = "all";
        selectedStream = "all";
        resetSchoolList();
        notifyDataSetChanged();
    }

    public String getSelectedRegion(){
        return selectedRegion;
    }

    public String getSelectedStream(){
        return selectedStream;
    }

    public void sort(int choice){

        switch(choice){
            case 0:
                Collections.sort(data, new Comparator<School>() {
                    @Override
                    public int compare(School o1, School o2) {
                        return o1.getSchoolName().compareTo(o2.getSchoolName());
                    }
                });
                notifyDataSetChanged();
                return;
            case 1:
                Collections.sort(data, new Comparator<School>() {
                    @Override
                    public int compare(School o1, School o2) {
                        return 0;
                    }
                });
                notifyDataSetChanged();
                return;
            case 2:
                Collections.sort(data, new Comparator<School>() {
                    @Override
                    public int compare(School o1, School o2) {
                        return o1.getRegion().compareTo(o2.getRegion());
                    }
                });
                notifyDataSetChanged();
                return;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView schoolImage;
        TextView schoolTitle, schoolDesc;
        ImageButton favIcon;
        Boolean success;
        ArrayList<School> favlist = new ArrayList<School>();

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            schoolImage = itemView.findViewById(R.id.schoolImage);
            schoolTitle = itemView.findViewById(R.id.schoolTitle);
            schoolDesc = itemView.findViewById(R.id.schoolDesc);
            favIcon = itemView.findViewById(R.id.starIcon);
            favIcon.setTag(R.drawable.ic_normalstar);

            user = FirebaseAuth.getInstance().getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference("Users");
            userID = user.getUid();

            // getting firebase reference
            reference.child(userID).child("favList").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapchild: snapshot.getChildren()) {
                        School sch = snapchild.getValue(School.class);
                        favlist.add(sch);
                    }
                    School school = data.get(getAdapterPosition());
                    for (School sch : favlist){
                        if (sch.getSchoolName().equals(school.getSchoolName())){
                            favIcon.setImageResource(R.drawable.ic_favstar);
                            favIcon.setTag(R.drawable.ic_favstar);
                            break;
                        } else {
                            favIcon.setImageResource(R.drawable.ic_normalstar);
                            favIcon.setTag(R.drawable.ic_normalstar);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(itemView.getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
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

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(v.getContext(), Details.class);
                    School school = data.get(getAdapterPosition());
                    i.putExtra("School", school);
                    v.getContext().startActivity(i);
                }
            });

            favIcon.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    School school =data.get(getAdapterPosition());
                    if (Integer.parseInt(favIcon.getTag().toString()) == R.drawable.ic_normalstar) {
                        int success = addSchoolToFav(favlist, school);
                        switch(success){
                            case 1:
                                favIcon.setImageResource(R.drawable.ic_favstar);
                                favIcon.setTag(R.drawable.ic_favstar);
                                Toast.makeText(v.getContext(), "School has been added to favourite list", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(v.getContext(), "School is already in favourite list", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(v.getContext(), "You already have more than 3 schools in your favourite list", Toast.LENGTH_SHORT).show();

                        }
                    }
                    else {
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

                private Boolean removeSchoolfromFav(ArrayList<School> favlist, School school) {
                    for (School sch : favlist){
                        if (sch.getSchoolName().equals(school.getSchoolName())){
                            favlist.remove(sch);
                            reference.child(userID).child("favList").setValue(favlist);
                            return true;
                        }
                    }
                    return false;
                }

                private int addSchoolToFav(ArrayList<School> favlist, School school) {
                    if (favlist.size() >= 3){
                        return 0;
                    }
                    for (School sch : favlist){
                        if (sch.getSchoolName().equals(school.getSchoolName())){
                            return 2;
                        }
                    }
                    if (favlist == null) {
                        favlist = new ArrayList<School>();
                    }
                    favlist.add(school);
                    reference.child(userID).child("favList").setValue(favlist);
                    return 1;
                }
            });
        }
    }
}