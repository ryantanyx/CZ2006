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

public class Details extends AppCompatActivity implements View.OnClickListener{

    TextView schName, schVision, schMission, schGender, schCutOff;
    ImageView schLogo, backbutton;
    ExpandableListView expandableListView;
    List<String> listGroup;
    ExpandableAdaptor adapter;
    HashMap<String, List<String>> listItem;

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
        ArrayList<String> schoolCCA = school.getCca();
        String subjects = school.getSubjects();
        ArrayList<String> schoolSubject = new ArrayList<String>();
        schoolSubject.add(subjects);
        ArrayList<String> schoolContact = school.getContactInfo();
        schoolContact.add(0, new String("Address: " + schoolAddress.toLowerCase()));
        ArrayList<String> schoolTransport = school.getTransport();

        listGroup = new ArrayList<String>();
        listItem = new HashMap<>();

        listGroup.add("CCA");
        listGroup.add("Subjects Offered");
        listGroup.add("Transport");
        listGroup.add("Contact Information");
        listItem.put(listGroup.get(0), schoolCCA);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.detailsBackButton:
                backbuttonmethod();
                break;
            default:
                break;
        }
    }

    private void backbuttonmethod() {
        Details.this.finish();
    }
}