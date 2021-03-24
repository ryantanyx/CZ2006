package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.renderscript.Sampler;
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
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{ //implements Filterable{

    private LayoutInflater layoutInflater;
    private List<School> data;
    private List<School> dataset;
    private String selectedRegion = "all";
    private String selectedStream = "all";
    private String currentSearchText ="";
    private String selectedCCA = "all";
    private int min = 0,max = 300;

    private Context context;
    private User userProfile;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    boolean flag = true;

    SearchAdapter(Context context){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        data = readSchoolData();
        data = readCCAData(data);
        data = readSubjectData(data);
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


    public void searchViewFilter(String newText) {
        currentSearchText = newText;
        resetSchoolList();
        filter();
    }

    public void filterRegion(String status) {
        resetRegion();
        selectedRegion = status;
        filter();
    }

    public void filterPSLE(int low, int high) {
        resetSchoolList();
        min = low;
        max = high;
        filter();
    }

    public void filterStream(String status) {
        resetStream();
        selectedStream = status;
        filter();
    }

    public void filterCCA(String status) {
        resetCCA();
        selectedCCA = status;
        filter();
    }

    public void filter() {
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

    private void resetSchoolList() {
        data.clear();
        data.addAll(dataset);
    }

    public void resetRegion() {
        selectedRegion = "all";
        resetSchoolList();
        notifyDataSetChanged();
    }

    public void resetStream() {
        selectedStream = "all";
        resetSchoolList();
        notifyDataSetChanged();
    }

    public void resetCCA() {
        selectedCCA = "all";
        resetSchoolList();
        notifyDataSetChanged();
    }

    public void resetFilter() {
        selectedRegion = "all";
        selectedStream = "all";
        resetSchoolList();
        notifyDataSetChanged();
    }

    public String getSelectedRegion() {
        return selectedRegion;
    }

    public String getSelectedStream() {
        return selectedStream;
    }

    public void sort(int choice) {

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
                        return o1.getRegion().compareTo(o2.getRegion());
                    }
                });
                notifyDataSetChanged();
                return;
            case 2:
                Collections.sort(data, new Comparator<School>() {
                    @Override
                    public int compare(School o1, School o2) {
                        return o1.getCutOffPoint().get("express").compareTo(o2.getCutOffPoint().get("express"));
                    }
                });
                notifyDataSetChanged();
                return;
            case 3:
                Collections.sort(data, new Comparator<School>() {
                    @Override
                    public int compare(School o1, School o2) {
                        return o1.getCutOffPoint().get("na").compareTo(o2.getCutOffPoint().get("na"));
                    }
                });
                notifyDataSetChanged();
                return;
            case 4:
                Collections.sort(data, new Comparator<School>() {
                    @Override
                    public int compare(School o1, School o2) {
                        return o1.getCutOffPoint().get("nt").compareTo(o2.getCutOffPoint().get("nt"));
                    }
                });
                notifyDataSetChanged();
                return;


        }
    }

    public void reverse() {Collections.reverse(data);
    }

    private List<School> readSchoolData() {
        List<School> schoolList = new ArrayList<>();
        try {
            InputStream is = context.getResources().openRawResource(R.raw.general_information_of_schools);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8)
            );
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {

                String[] tokens = line.split("\t");

                if (tokens[27].equalsIgnoreCase(context.getString(R.string.sch_level)) || tokens[27].equalsIgnoreCase("MIXED LEVELS")){
                    ArrayList<String> contact = new ArrayList<String>();
                    ArrayList<String> transport = new ArrayList<String>();
                    HashMap<String, Integer> cut_off= new HashMap<String, Integer>();

                    School school = new School();
                    school.setImageUrl(tokens[0]);
                    school.setSchoolName(tokens[1]);
                    school.setAddress(tokens[3]);
                    school.setMission(tokens[20]);
                    school.setVision(tokens[19]);
                    school.setLocation(tokens[22]);
                    school.setRegion(tokens[23]);
                    school.setType(tokens[24]);
                    school.setGender(tokens[25]);

                    LatLng latlong = getLocationFromAddress(context, tokens[3]);
                    school.setLat(latlong.latitude);
                    school.setLng(latlong.longitude);

                    cut_off.put("express", Integer.parseInt(tokens[36]));
                    cut_off.put("na", Integer.parseInt(tokens[37]));
                    cut_off.put("nt", Integer.parseInt(tokens[38]));
                    school.setCutOffPoint(cut_off);

                    contact.add("Tel no: " + tokens[5]);
                    contact.add("Email address: "  + tokens[9].toLowerCase());
                    school.setContactInfo(contact);
                    if (tokens[10].contains("\"")){
                        transport.add("By MRT: " + tokens[10].substring(1,tokens[10].length() -1).toLowerCase());
                    } else{
                        transport.add("By MRT: " + tokens[10].toLowerCase());
                    }
                    if (tokens[11].contains("\"")){
                        transport.add("By bus: " + tokens[11].substring(1,tokens[11].length() -1));
                    } else{
                        transport.add("By bus: " + tokens[11]);
                    }
                    school.setTransport(transport);

                    schoolList.add(school);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return schoolList;
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    private List<School> readCCAData(List<School> schoolList) {
        Set<String> set_sports = new HashSet<String>(), set_vpa = new HashSet<String>(), set_cs = new HashSet<String>(), set_ug = new HashSet<String>();
        ArrayList<String> temp;
        HashMap<String, ArrayList<String>> ccas;
        HashMap<String, HashMap<String, ArrayList<String>>> schCCA = new HashMap<String, HashMap<String, ArrayList<String>>>();

        try {
            InputStream is = context.getResources().openRawResource(R.raw.co_curricular_activities_ccas);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8)
            );
            String line;
            int i;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String cca, ccaType;
                String[] tokens = line.split("\t");
                if (tokens[1].equalsIgnoreCase(context.getString(R.string.sch_level))){
                    if (!schCCA.containsKey(tokens[0])){
                        ccas = new HashMap<String, ArrayList<String>>();
                        schCCA.put(tokens[0], ccas);
                    }
                    switch(tokens[2]) {
                        case "PHYSICAL SPORTS":
                            if (schCCA.get(tokens[0]).containsKey("Sports")){
                                temp = new ArrayList<String>(schCCA.get(tokens[0]).get("Sports"));
                            } else {
                                temp = new ArrayList<String>();
                            }
                            ccas = schCCA.get(tokens[0]);
                            temp.add(tokens[3].toUpperCase());
                            ccas.put("Sports", temp);
                            set_sports.addAll(temp);
                            schCCA.put(tokens[0], ccas);
                            break;

                        case "VISUAL AND PERFORMING ARTS":
                            if (schCCA.get(tokens[0]).containsKey("Performing Arts")){
                                temp = new ArrayList<String>(schCCA.get(tokens[0]).get("Performing Arts"));
                            } else {
                                temp = new ArrayList<String>();
                            }
                            ccas = schCCA.get(tokens[0]);
                            temp.add(tokens[3].toUpperCase());
                            ccas.put("Performing Arts", temp);
                            set_vpa.addAll(temp);
                            schCCA.put(tokens[0], ccas);
                            break;
                        case "CLUBS AND SOCIETIES":
                            if (schCCA.get(tokens[0]).containsKey("Clubs & Societies")){
                                temp = new ArrayList<String>(schCCA.get(tokens[0]).get("Clubs & Societies"));
                            } else {
                                temp = new ArrayList<String>();
                            }
                            ccas = schCCA.get(tokens[0]);
                            temp.add(tokens[3].toUpperCase());
                            ccas.put("Clubs & Societies", temp);
                            set_cs.addAll(temp);
                            schCCA.put(tokens[0], ccas);
                            break;
                        case "UNIFORMED GROUPS":
                            if (schCCA.get(tokens[0]).containsKey("Uniformed Groups")){
                                temp = new ArrayList<String>(schCCA.get(tokens[0]).get("Uniformed Groups"));
                            } else {
                                temp = new ArrayList<String>();
                            }
                            ccas = schCCA.get(tokens[0]);
                            temp.add(tokens[3].toUpperCase());
                            ccas.put("Uniformed Groups", temp);
                            set_ug.addAll(temp);
                            schCCA.put(tokens[0], ccas);
                            break;
                        default:
                            continue;
                    }
                }
            }
            for (School school: schoolList){
                if (schCCA.containsKey(school.getSchoolName())){
                    school.setCca(schCCA.get(school.getSchoolName()));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return schoolList;
    }

    private List<School> readSubjectData(List<School> schoolList) {
        HashMap<String, String> schSubject = new HashMap<String, String>();

        try {
            InputStream is = context.getResources().openRawResource(R.raw.subjects_offered);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8)
            );
            String line;
            int i;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String subject;
                String[] tokens = line.split("\t");
                if (!schSubject.containsKey(tokens[0])){
                    String subjects = new String("");
                    schSubject.put(tokens[0], subjects);
                }
                if (schSubject.get(tokens[0]).equals("")){
                    subject = new String("Subjects: " + schSubject.get(tokens[0]) + tokens[1].toLowerCase());
                } else{
                    subject = new String(schSubject.get(tokens[0]) + ", " + tokens[1].toLowerCase());
                }
                schSubject.put(tokens[0], subject);
            }
            for (School school: schoolList){
                if (schSubject.containsKey(school.getSchoolName())){
                    school.setSubjects(schSubject.get(school.getSchoolName()));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return schoolList;
    }

    public List<School> getSchoolList() {
        return dataset;
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
