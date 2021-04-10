package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents the Search Adapter Controller which controls the functions related to searching for schools
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{
    /**
     * Distance between school and user's address
     */
    private static double schDistance;
    /**
     * Instance of the MapController
     */
    private static MapController MapController;
    /**
     * User accessing the application
     */
    private static User User;
    /**
     * The current context of the application
     */
    private static Context context;
    /**
     * Boolean Flag
     */
    boolean flag = true;
    /**
     * Layout inflater to instantiate layout from XML file
     */
    private LayoutInflater layoutInflater;
    /**
     * List of schools used by the adapter
     */
    private List<School> data;
    /**
     * List of schools available in dataset
     */
    private List<School> dataset;
    /**
     * Status of region filter
     */
    private String selectedRegion = "all";
    /**
     * Status of stream filter
     */
    private String selectedStream = "all";
    /**
     * Current text in search bar
     */
    private String currentSearchText ="";
    /**
     * Status of CCA filter
     */
    private String selectedCCA = "all";
    /**
     * PSLE cutoff and distance slider values
     */
    private int pslemin = 0,pslemax = 300,distmin=0,distmax=50;
    /**
     * User accessing the application
     */
    private User userProfile;
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
     * User's address in LatLng format
     */
    private LatLng userLocation;
    /**
     * HashMap mapping the distance between the school and user's location to each school
     */
    private HashMap<String, Double> schDistList = new HashMap<>();
    /**
     * Ordered HashMap mapping the distance between the school and user's location to each school
     */
    private HashMap<String, Double> schOrderedDistList;
    /**
     * Snapshot of the data in Firebase
     */
    private DataSnapshot snapshot;

    /**
     * Constructor to create new Search Adapter
     * @param context The current context of the application
     */
    SearchAdapter(Context context){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        secondThread runnable = new secondThread();
        runnable.run();
        this.dataset = new ArrayList<School>(data);
    }

    /**
     * Calculate the distance between the user's address and each of the school
     * @param schoolList The list of schools
     * @param userPosition The user's address
     * @return HashMap mapping the distance between the school and user's location to each school
     */
    public static HashMap<String, Double> getSchDist(List<School> schoolList, LatLng userPosition) {
        HashMap<String, Double> schDist = new HashMap<>();
        HashMap<String, LatLng> schLocation = new HashMap<>();
        schLocation = MapController.getLatLong(context, schoolList);
        for (Map.Entry<String, LatLng> entry : schLocation.entrySet()) {
            schDistance = MapController.distance(userPosition.latitude, userPosition.longitude, entry.getValue().latitude, entry.getValue().longitude);
            schDist.put(entry.getKey(), schDistance);
        }
            return schDist;
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
     * @param holders The view holder whose contents should be updated
     * @param position The position of the holder with respect to this adapter
     */
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
    /**
     * Get the size of the list of schools
     * @return integer to indicate size of the list of schools
     */
    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * Filters the list of schools based on text entered in search bar
     * @param newText The text entered in search bar
     */
    public void searchViewFilter(String newText) {
        currentSearchText = newText;
        resetSchoolList();
        filter();
    }

    /**
     * Filters the list of schools based on region selected by the user
     * @param status The region selected by the user
     */
    public void filterRegion(String status) {
        resetRegion();
        selectedRegion = status;
        filter();
    }

    /**
     * Filters the list of schools based on PSLE cut-off score range selected by the user
     * @param low The lower limit set by the user
     * @param high The upper limit set by the user
     */
    public void filterPSLE(int low, int high) {
        resetSchoolList();
        pslemin = low;
        pslemax = high;
        filter();
    }
    /**
     * Filters the list of schools based on distance from home selected by the user
     * @param low The lower limit set by the user
     * @param high The upper limit set by the user
     */
    public void filterDist(int low, int high) {
        resetSchoolList();
        distmin = low;
        distmax = high;
        if (schDistList.isEmpty())
            schDistList = getSchDist(data, userLocation);
        filter();
    }

    /**
     * Filters the list of schools based on stream selected by the user
     * @param status The stream selected by the user
     */
    public void filterStream(String status) {
        resetStream();
        selectedStream = status;
        filter();
    }
    /**
     * Filters the list of schools based on CCA selected by the user
     * @param status The CCA selected by the user
     */
    public void filterCCA(String status) {
        resetCCA();
        selectedCCA = status;
        filter();
    }
    /**
     * Filters the list of schools based on the filter selection of the user
     */
    public void filter() {
        List<School> filteredList = new ArrayList<>();
        for(School school:dataset) {
            if (!selectedStream.equals("all")) {
                if (!school.getCutOffPoint().get(selectedStream).equals(0)) {
                    if (school.getCutOffPoint().get(selectedStream) >= pslemin && school.getCutOffPoint().get(selectedStream) <= pslemax) {
                        if (!selectedRegion.equals("all")) {
                            if (school.getRegion().toLowerCase().contains(selectedRegion)) {
                                if (!selectedCCA.equals("all")) {
                                    if (school.getCca()!=null) {
                                        for (Map.Entry<String,ArrayList<String>> temp : school.getCca().entrySet()) {
                                            ArrayList<String> strings = temp.getValue();
                                            if (strings.contains(selectedCCA)) {
                                                if (schDistList.get(school.getSchoolName())!= null) {
                                                    if (schDistList.get(school.getSchoolName()) >= distmin && schDistList.get(school.getSchoolName()) <= distmax) {
                                                        if (currentSearchText.equals(""))
                                                            filteredList.add(school);
                                                        else {
                                                            if (school.getSchoolName().toLowerCase().contains(currentSearchText.toLowerCase()))
                                                                filteredList.add(school);
                                                        }
                                                    }
                                                }else {
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
                                } else {
                                    if (schDistList.get(school.getSchoolName())!= null) {
                                        if (schDistList.get(school.getSchoolName()) >= distmin && schDistList.get(school.getSchoolName()) <= distmax) {
                                            if (currentSearchText.equals(""))
                                                filteredList.add(school);
                                            else {
                                                if (school.getSchoolName().toLowerCase().contains(currentSearchText.toLowerCase()))
                                                    filteredList.add(school);
                                            }
                                        }
                                    }else {
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
                            if (!selectedCCA.equals("all")) {
                                if (school.getCca()!=null) {
                                    for (Map.Entry<String,ArrayList<String>> temp : school.getCca().entrySet()) {
                                        ArrayList<String> strings = temp.getValue();
                                        if (strings.contains(selectedCCA)) {
                                            if (schDistList.get(school.getSchoolName())!= null) {
                                                if (schDistList.get(school.getSchoolName()) >= distmin && schDistList.get(school.getSchoolName()) <= distmax) {
                                                    if (currentSearchText.equals(""))
                                                        filteredList.add(school);
                                                    else {
                                                        if (school.getSchoolName().toLowerCase().contains(currentSearchText.toLowerCase()))
                                                            filteredList.add(school);
                                                    }
                                                }
                                            }else {
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
                            } else {
                                if (schDistList.get(school.getSchoolName())!= null) {
                                    if (schDistList.get(school.getSchoolName()) >= distmin && schDistList.get(school.getSchoolName()) <= distmax) {
                                        if (currentSearchText.equals(""))
                                            filteredList.add(school);
                                        else {
                                            if (school.getSchoolName().toLowerCase().contains(currentSearchText.toLowerCase()))
                                                filteredList.add(school);
                                        }
                                    }
                                }else {
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
            }
            else {
                if ((school.getCutOffPoint().get("express") >= pslemin && school.getCutOffPoint().get("express") <= pslemax)
                        || (school.getCutOffPoint().get("na") != 0 && school.getCutOffPoint().get("na") >= pslemin
                        && school.getCutOffPoint().get("na") <= pslemax) || (school.getCutOffPoint().get("nt") != 0
                        && school.getCutOffPoint().get("nt") >= pslemin && school.getCutOffPoint().get("nt") <= pslemax)) {
                    if (!selectedRegion.equals("all")) {
                        if (school.getRegion().toLowerCase().contains(selectedRegion)) {
                            if (!selectedCCA.equals("all")) {
                                if (school.getCca()!=null) {
                                    for (Map.Entry<String,ArrayList<String>> temp : school.getCca().entrySet()) {
                                        ArrayList<String> strings = temp.getValue();
                                        if (strings.contains(selectedCCA)) {
                                            if (schDistList.get(school.getSchoolName())!= null) {
                                                if (schDistList.get(school.getSchoolName()) >= distmin && schDistList.get(school.getSchoolName()) <= distmax) {
                                                    if (currentSearchText.equals(""))
                                                        filteredList.add(school);
                                                    else {
                                                        if (school.getSchoolName().toLowerCase().contains(currentSearchText.toLowerCase()))
                                                            filteredList.add(school);
                                                    }
                                                }
                                            }else {
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
                            } else {
                                if (schDistList.get(school.getSchoolName())!= null) {
                                    if (schDistList.get(school.getSchoolName()) >= distmin && schDistList.get(school.getSchoolName()) <= distmax) {
                                        if (currentSearchText.equals(""))
                                            filteredList.add(school);
                                        else {
                                            if (school.getSchoolName().toLowerCase().contains(currentSearchText.toLowerCase()))
                                                filteredList.add(school);
                                        }
                                    }
                                }else {
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
                        if (!selectedCCA.equals("all")) {
                            if (school.getCca()!=null) {
                                for (Map.Entry<String,ArrayList<String>> temp : school.getCca().entrySet()) {
                                    ArrayList<String> strings = temp.getValue();
                                    if (strings.contains(selectedCCA)) {
                                        if (schDistList.get(school.getSchoolName())!= null) {
                                            if (schDistList.get(school.getSchoolName()) >= distmin && schDistList.get(school.getSchoolName()) <= distmax) {
                                                if (currentSearchText.equals(""))
                                                    filteredList.add(school);
                                                else {
                                                    if (school.getSchoolName().toLowerCase().contains(currentSearchText.toLowerCase()))
                                                        filteredList.add(school);
                                                }
                                            }
                                        }else {
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
                        } else {
                            if (schDistList.get(school.getSchoolName())!= null) {
                                if (schDistList.get(school.getSchoolName()) >= distmin && schDistList.get(school.getSchoolName()) <= distmax) {
                                    if (currentSearchText.equals(""))
                                        filteredList.add(school);
                                    else {
                                        if (school.getSchoolName().toLowerCase().contains(currentSearchText.toLowerCase()))
                                            filteredList.add(school);
                                    }
                                }
                            }else {
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
        }
        data.clear();
        data.addAll(filteredList);
        notifyDataSetChanged();
    }
    /**
     * Resets the school list in the adapter
     */
    private void resetSchoolList() {
        data.clear();
        data.addAll(dataset);
    }

    /**
     * Resets the region filter selected by the user
     */
    public void resetRegion() {
        selectedRegion = "all";
        resetSchoolList();
        notifyDataSetChanged();
    }
    /**
     * Resets the stream filter selected by the user
     */
    public void resetStream() {
        selectedStream = "all";
        resetSchoolList();
        notifyDataSetChanged();
    }
    /**
     * Resets the CCA filter selected by the user
     */
    public void resetCCA() {
        selectedCCA = "all";
        resetSchoolList();
        notifyDataSetChanged();
    }
    /**
     * Resets both the region and stream filter selected by the user
     */
    public void resetFilter() {
        selectedRegion = "all";
        selectedStream = "all";
        resetSchoolList();
        notifyDataSetChanged();
    }

    /**
     * Get the region filter selected by the user
     * @return The region filter selected by the user
     */
    public String getSelectedRegion() {
        return selectedRegion;
    }

    /**
     * Get the stream filter selected by the user
     * @return The stream filter selected by the user
     */
    public String getSelectedStream() {
        return selectedStream;
    }

    /**
     * Sort the list of school based on the user's choice
     * @param choice The sort settings selected by the user
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sort(int choice) {
        List<School> filteredList = new ArrayList<>();
        //resetSchoolList();
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
//                for(School school:dataset){
//                    if(school.getCutOffPoint().get("express") != 0){
//                        filteredList.add(school);
//                    }
//                }
//                data.clear();
//                data.addAll(filteredList);
                Collections.sort(data, new Comparator<School>() {
                    @Override
                    public int compare(School o1, School o2) {
                        return o1.getCutOffPoint().get("express").compareTo(o2.getCutOffPoint().get("express"));
                    }
                });

                notifyDataSetChanged();
                return;
            case 3:
//                for(School school:dataset){
//                    if(school.getCutOffPoint().get("na") != 0){
//                        filteredList.add(school);
//                    }
//                }
//                data.clear();
//                data.addAll(filteredList);
                Collections.sort(data, new Comparator<School>() {
                    @Override
                    public int compare(School o1, School o2) {
                        return o1.getCutOffPoint().get("na").compareTo(o2.getCutOffPoint().get("na"));
                    }
                });

                notifyDataSetChanged();
                return;
            case 4:
//                for(School school:dataset){
//                    if(school.getCutOffPoint().get("nt") != 0){
//                        filteredList.add(school);
//                    }
//                }
//                data.clear();
//                data.addAll(filteredList);
                Collections.sort(data, new Comparator<School>() {
                    @Override
                    public int compare(School o1, School o2) {
                        return o1.getCutOffPoint().get("nt").compareTo(o2.getCutOffPoint().get("nt"));
                    }
                });
                notifyDataSetChanged();
                return;
            case 5:
                /*LatLngBounds sgBounds = new LatLngBounds(
                        new LatLng(1.264850, 103.622483), // SW bounds
                        new LatLng(1.475187, 104.016803)  // NE bounds
                );

                if (userLocation == null) {
                    userLocation = sgBounds.getCenter();
                }*/

                secondThread runnable = new secondThread();
                runnable.run(data, userLocation);

                List<Map.Entry<String,Double>> list = new LinkedList<Map.Entry<String, Double> >(schDistList.entrySet());
                Collections.sort(list, new Comparator<Map.Entry<String, Double> >() {
                    public int compare(Map.Entry<String, Double> o1,
                                       Map.Entry<String, Double> o2)
                    {
                        return (o1.getValue()).compareTo(o2.getValue());
                    }
                });
                HashMap<String, Double> schOrderedDistList = new LinkedHashMap<String, Double>();
                for (Map.Entry<String, Double> aa : list) {
                    schOrderedDistList.put(aa.getKey(), aa.getValue());
                }
                ArrayList<String> schools = new ArrayList<>(schOrderedDistList.keySet());
                data.sort(Comparator.comparing(v->schools.indexOf(v.getSchoolName())));
                notifyDataSetChanged();
                return;
        };

    }

    /**
     * Reverse the order of the data
     */
    public void reverse() {Collections.reverse(data);
    }

    /**
     * Read the school data from the text file
     * @return The list of schools
     */
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

    /**
     * Read and update the list of schools with the CCA data from the text file
     * @param schoolList The list of schools
     * @return The updated list of schools with the CCA data
     */
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

    /**
     * Read and updates the list of school with the subjects offered data from the text file
     * @param schoolList The list of school
     * @return The updated list of school with the subjects offered data
     */
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

    /**
     * Get the list of schools available
     * @return The list of schools
     */
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
                        if (snapchild != null){
                            School sch = snapchild.getValue(School.class);
                            favlist.add(sch);
                        }
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
                                Toast.makeText(v.getContext(), "You already have 3 schools in your favourite list", Toast.LENGTH_SHORT).show();

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

    class secondThread implements Runnable {

        secondThread(){
        }

        @Override
        public void run() {
            data = readSchoolData();
            data = readCCAData(data);
            data = readSubjectData(data);

            user = FirebaseAuth.getInstance().getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference("Users");
            userID = user.getUid();

            reference.child(userID).child("address").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String userAddress = snapshot.getValue(String.class);
                    if (userAddress != null){
                        userLocation = MapController.getLocationFromAddress(context, userAddress);
                        schDistList = new HashMap<>();
                        schDistList = getSchDist(data, userLocation);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context, "Something went wrong!", Toast.LENGTH_LONG).show();
                }
            });
        }

        public void run(List<School> data, LatLng userLocation){
            if (schDistList == null){
                schDistList = new HashMap<>();
                schDistList = getSchDist(data, userLocation);
            }
        }
    }
}
