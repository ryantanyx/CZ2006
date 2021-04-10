package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the Details Page Boundary which displays the information of the school selected
 */
public class Details extends AppCompatActivity implements View.OnClickListener{
    /**
     * TextView to display name, vision, mission, gender status, and PSLE cut-off points of the school selected
     */
    TextView schName, schVision, schMission, schGender, schCutOff;
    /**
     * ImageView to display school logo and back button
     */
    ImageView schLogo, backbutton;
    /**
     * ExpandableListView to contain the school information
     */
    ExpandableListView expandableListView;
    /**
     * List to store the different groups required
     */
    List<String> listGroup;
    /**
     * ExpandableAdaptor to display the relevant school information
     */
    ExpandableAdaptor adapter;
    /**
     * Hashmap to store the information under a specific group
     */
    HashMap<String, List<String>> listItem;
    /**
     * Creation of activity from savedInstanceState and setting the layout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        schName = findViewById(R.id.schName);
        schLogo = findViewById(R.id.schLogo);
        schVision = findViewById(R.id.schVision);
        schMission = findViewById(R.id.schMission);
        schGender = findViewById(R.id.schGender);
        schCutOff = findViewById(R.id.schCutOff);
        expandableListView = findViewById(R.id.expandable_listview);

        backbutton =  (ImageView) findViewById(R.id.detailsBackButton);
        backbutton.setOnClickListener(this);

        Intent i = getIntent();
        School school = i.getParcelableExtra("School");

        String imageUrl = school.getImageUrl();
        String schoolName = school.getSchoolName();
        String schoolAddress = school.getAddress();
        String schoolMission = school.getMission();
        String schoolVision = school.getVision();
        String schoolGender = school.getGender();
        HashMap<String, Integer> schoolCutOff = school.getCutOffPoint();
        HashMap<String, ArrayList<String>> schoolCCA = school.getCca();

        String subjects = school.getSubjects();
        ArrayList<String> schoolSubject = new ArrayList<String>();
        schoolSubject.add(subjects);
        ArrayList<String> schoolContact = school.getContactInfo();
        schoolContact.add(0, new String("Address: " + schoolAddress.toLowerCase()));
        ArrayList<String> schoolTransport = school.getTransport();

        ArrayList<String> cca = new ArrayList<String>();
        ArrayList<String> type = new ArrayList<String>();
        type.add("Sports");
        type.add("Performing Arts");
        type.add("Clubs & Societies");
        type.add("Uniformed Groups");
        ArrayList<String> temp = new ArrayList<String>();
        String entry;
        for (String ccatype : type){
            temp = schoolCCA.get(ccatype);
            entry = new String(ccatype + ": ");
            if (temp != null){
                for (String s : temp) {
                    entry = entry + s.toLowerCase() + ", ";
                }
            }
            cca.add(entry.substring(0,entry.length()-1));
        }

        listGroup = new ArrayList<String>();
        listItem = new HashMap<>();

        listGroup.add("CCA");
        listGroup.add("Subjects Offered");
        listGroup.add("Transport");
        listGroup.add("Contact Information");

        listItem.put(listGroup.get(0), cca);
        listItem.put(listGroup.get(1), schoolSubject);
        listItem.put(listGroup.get(2), schoolTransport);
        listItem.put(listGroup.get(3), schoolContact);
        adapter = new ExpandableAdaptor(this, listGroup, listItem);
        expandableListView.setAdapter(adapter);

        String points = "";
        if (schoolCutOff.get("express") != 0){
            points = new String(points + "Express: " + schoolCutOff.get("express").toString() + "\n");
        }
        if (schoolCutOff.get("na") != 0){
            points = new String(points + "Normal Academic: " + schoolCutOff.get("na").toString() + "\n");
        }
        if (schoolCutOff.get("nt") != 0) {
            points = new String(points + "Normal Technical: " + schoolCutOff.get("nt").toString() + "\n");
        }
        if (!points.equals("")) {
            points = new String("Cut off point: \n\n" + points);
        }

        schName.setText(schoolName);
        schVision.setText("Vision:" + "\n" + schoolVision);
        schMission.setText("Mission:" + "\n" + schoolMission);
        schCutOff.setText(points);
        schGender.setText("School Type: " + schoolGender.toLowerCase());
        Glide.with(this).load(imageUrl).error(R.drawable.ic_person).into(schLogo);
    }

    /**
     * Switch case to execute different commands for the respective buttons
     * @param v View
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.detailsBackButton:
                this.finish();
                break;
            default:
                break;
        }
    }
}