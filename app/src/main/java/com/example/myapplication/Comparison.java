package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Comparison extends AppCompatActivity implements View.OnClickListener{

    TextView schName2, schVision2, schMission2, schGender2, schCutOff2,schName1, schVision1, schMission1, schGender1, schCutOff1;
    ImageView schLogo2, schLogo1, backbutton;
    ExpandableListView expandableListView2, expandableListView1;
    List<String> listGroup2, listGroup1;
    ExpandableAdaptor adapter2, adapter1;
    HashMap<String, List<String>> listItem2,listItem1;
    School school1, school2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: Create comparison layout
        setContentView(R.layout.fragment_comparing);

        schName1 = findViewById(R.id.schName1);
        schLogo1 = findViewById(R.id.schLogo1);
        schVision1 = findViewById(R.id.schVision1);
        schMission1 = findViewById(R.id.schMission1);
        schGender1 = findViewById(R.id.schGender1);
        schCutOff1 = findViewById(R.id.schCutOff1);
        expandableListView1 = findViewById(R.id.expandableListView1);

        schName2 = findViewById(R.id.schName2);
        schLogo2 = findViewById(R.id.schLogo2);
        schVision2 = findViewById(R.id.schVision2);
        schMission2 = findViewById(R.id.schMission2);
        schGender2 = findViewById(R.id.schGender2);
        schCutOff2 = findViewById(R.id.schCutOff2);
        expandableListView2 = findViewById(R.id.expandableListView2);

        backbutton =  (ImageView) findViewById(R.id.comparisonBackButton);
        backbutton.setOnClickListener(this);

        Intent i = getIntent();
        ArrayList<School> school = i.getParcelableArrayListExtra("Selected");
        school1 = school.get(0);
        school2 = school.get(1);

        //Setting details of school 1 (Left side)
        String imageUrl1 = school1.getImageUrl();
        String schoolName1 = school1.getSchoolName();
        String schoolAddress1 = school1.getAddress();
        String schoolMission1 = school1.getMission();
        String schoolVision1 = school1.getVision();
        String schoolGender1 = school1.getGender();
        HashMap<String, Integer> schoolCutOff1 = school1.getCutOffPoint();
        HashMap<String, ArrayList<String>> schoolCCA1 = school1.getCca();

        String subjects1 = school1.getSubjects();
        ArrayList<String> schoolSubject1 = new ArrayList<String>();
        schoolSubject1.add(subjects1);
        ArrayList<String> schoolContact1 = school1.getContactInfo();
        schoolContact1.add(0, new String("Address: " + schoolAddress1.toLowerCase()));
        ArrayList<String> schoolTransport1 = school1.getTransport();

        ArrayList<String> cca1 = new ArrayList<String>();
        ArrayList<String> type1 = new ArrayList<String>();
        type1.add("Sports");
        type1.add("Performing Arts");
        type1.add("Clubs & Societies");
        type1.add("Uniformed Groups");
        type1.add("Others");
        ArrayList<String> temp = new ArrayList<String>();
        String entry;
        for (String ccatype : type1){
            temp = schoolCCA1.get(ccatype);
            entry = new String(ccatype + ": ");
            if (temp != null){
                for (String s : temp) {
                    entry = entry + s + ", ";
                }
            }
            cca1.add(entry.substring(0,entry.length()-1));
        }

        listGroup1 = new ArrayList<String>();
        listItem1 = new HashMap<>();

        listGroup1.add("CCA");
        listGroup1.add("Subjects Offered");
        listGroup1.add("Transport");
        listGroup1.add("Contact Information");

        listItem1.put(listGroup1.get(0), cca1);
        listItem1.put(listGroup1.get(1), schoolSubject1);
        listItem1.put(listGroup1.get(2), schoolTransport1);
        listItem1.put(listGroup1.get(3), schoolContact1);
        adapter1 = new ExpandableAdaptor(this, listGroup1, listItem1, 16);
        expandableListView1.setAdapter(adapter1);

        String points = "";
        if (schoolCutOff1.get("express") != 0){
            points = new String(points + "Express: " + schoolCutOff1.get("express").toString() + "\n");
        }
        if (schoolCutOff1.get("na") != 0){
            points = new String(points + "Normal Academic: " + schoolCutOff1.get("na").toString() + "\n");
        }
        if (schoolCutOff1.get("nt") != 0) {
            points = new String(points + "Normal Technical: " + schoolCutOff1.get("nt").toString() + "\n");
        }
        if (!points.equals("")) {
            points = new String("Cut off point: \n\n" + points);
        }

        schName1.setText(schoolName1);
        schVision1.setText("Vision:" + "\n" + schoolVision1);
        schMission1.setText("Mission:" + "\n" + schoolMission1);
        schCutOff1.setText(points);
        schGender1.setText("School Type: " + schoolGender1.toLowerCase());
        Glide.with(this).load(imageUrl1).error(R.drawable.ic_person).into(schLogo1);

        //Setting details of school 2 (Right side)
        String imageUrl2 = school2.getImageUrl();
        String schoolName2 = school2.getSchoolName();
        String schoolAddress2 = school2.getAddress();
        String schoolMission2 = school2.getMission();
        String schoolVision2 = school2.getVision();
        String schoolGender2 = school2.getGender();
        HashMap<String, Integer> schoolCutOff2 = school2.getCutOffPoint();
        HashMap<String, ArrayList<String>> schoolCCA2 = school2.getCca();

        String subjects2 = school2.getSubjects();
        ArrayList<String> schoolSubject2 = new ArrayList<String>();
        schoolSubject2.add(subjects2);
        ArrayList<String> schoolContact2 = school2.getContactInfo();
        schoolContact2.add(0, new String("Address: " + schoolAddress2.toLowerCase()));
        ArrayList<String> schoolTransport2 = school2.getTransport();

        ArrayList<String> cca2 = new ArrayList<String>();
        ArrayList<String> type2 = new ArrayList<String>();
        type2.add("Sports");
        type2.add("Performing Arts");
        type2.add("Clubs & Societies");
        type2.add("Uniformed Groups");
        type2.add("Others");
        for (String ccatype : type2){
            temp = schoolCCA2.get(ccatype);
            entry = new String(ccatype + ": ");
            if (temp != null){
                for (String s : temp) {
                    entry = entry + s + ", ";
                }
            }
            cca2.add(entry.substring(0,entry.length()-1));
        }

        listGroup2 = new ArrayList<String>();
        listItem2 = new HashMap<>();

        listGroup2.add("CCA");
        listGroup2.add("Subjects Offered");
        listGroup2.add("Transport");
        listGroup2.add("Contact Information");

        listItem2.put(listGroup2.get(0), cca2);
        listItem2.put(listGroup2.get(1), schoolSubject2);
        listItem2.put(listGroup2.get(2), schoolTransport2);
        listItem2.put(listGroup2.get(3), schoolContact2);
        adapter2 = new ExpandableAdaptor(this, listGroup2, listItem2, 16);
        expandableListView2.setAdapter(adapter2);

        points = new String("");
        if (schoolCutOff2.get("express") != 0){
            points = new String(points + "Express: " + schoolCutOff2.get("express").toString() + "\n");
        }
        if (schoolCutOff2.get("na") != 0){
            points = new String(points + "Normal Academic: " + schoolCutOff2.get("na").toString() + "\n");
        }
        if (schoolCutOff2.get("nt") != 0) {
            points = new String(points + "Normal Technical: " + schoolCutOff2.get("nt").toString() + "\n");
        }
        if (!points.equals("")) {
            points = new String("Cut off point: \n\n" + points);
        }

        schName2.setText(schoolName2);
        schVision2.setText("Vision:" + "\n" + schoolVision2);
        schMission2.setText("Mission:" + "\n" + schoolMission2);
        schCutOff2.setText(points);
        schGender2.setText("School Type: " + schoolGender2.toLowerCase());
        Glide.with(this).load(imageUrl2).error(R.drawable.ic_person).into(schLogo2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.comparisonBackButton:
                this.finish();
                break;
            default:
                break;
        }
    }
}
