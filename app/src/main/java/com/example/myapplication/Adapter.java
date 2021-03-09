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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements Filterable{

    private LayoutInflater layoutInflater;
    private List<School> data;
    private List<School> dataset;
    private String selectedFilter = "all";
    private String currentSearchText ="";
    boolean flag = true;

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

    public void searchViewFilter(String newText){
        currentSearchText = newText;
        ArrayList<School> filteredList = new ArrayList<School>();
        resetSchoolList();
        for(School school:dataset) {
            if (school.getSchoolName().toLowerCase().contains(newText.toLowerCase()))
            {
                if (selectedFilter.equals("all"))
                    filteredList.add(school);
                else {
                    if (school.getRegion().toLowerCase().contains(selectedFilter))
                        filteredList.add(school);
                }
            }
        }
        data.clear();
        data.addAll(filteredList);
        notifyDataSetChanged();
    }

    public void filterRegion(String status){
        List<School> filteredList = new ArrayList<>();
        resetFilter();
        selectedFilter = status;
        for(School school:dataset)
        {
            if(school.getRegion().toLowerCase().contains(status)) {
                if (currentSearchText=="")
                    filteredList.add(school);
                else{
                    if (school.getSchoolName().toLowerCase().contains(currentSearchText.toLowerCase()))
                        filteredList.add(school);
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

    public void resetFilter(){
        selectedFilter = "all";
        resetSchoolList();
        notifyDataSetChanged();
    }

    public String getSelectedFilter(){
        return selectedFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView schoolImage;
        TextView schoolTitle, schoolDesc;
        ImageButton favIcon;
        Boolean flag = true;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            schoolImage = itemView.findViewById(R.id.schoolImage);
            schoolTitle = itemView.findViewById(R.id.schoolTitle);
            schoolDesc = itemView.findViewById(R.id.schoolDesc);
            favIcon = itemView.findViewById(R.id.starIcon);
            favIcon.setImageResource(R.drawable.ic_normalstar);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(v.getContext(), Details.class);
                    School school = data.get(getAdapterPosition());
                    i.putExtra("School", data.get(getAdapterPosition()));
                    v.getContext().startActivity(i);
                }
            });
            favIcon.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //favIcon.setSelected(!favIcon.isPressed());
                    if (flag) {
                        //addSchoolToFav();       //Remove comment to test
                        favIcon.setImageResource(R.drawable.ic_favstar);
                        Toast.makeText(v.getContext(), "School has been added to favourite list", Toast.LENGTH_SHORT).show();
                        flag = false;
                    }
                    else {
                        //removeSchoolfromFav();      //Remove comment to test
                        favIcon.setImageResource(R.drawable.ic_normalstar);
                        Toast.makeText(v.getContext(), "School has been removed from favourite list", Toast.LENGTH_SHORT).show();
                        flag = true;
                    }
                }

                private void removeSchoolfromFav() {
                }

                private void addSchoolToFav() {
                }
            });
        }
    }
}
