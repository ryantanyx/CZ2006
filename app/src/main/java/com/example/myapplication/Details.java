package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Details extends AppCompatActivity {

    TextView schName, schVision, schMission, schGender, schCutOff;
    ImageView schLogo;
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
        //TODO: programme and cca expandable adaptor


        Intent i = getIntent();
        School school = i.getParcelableExtra("School");

        String imageUrl = school.getImageUrl();
        String schoolName = school.getSchoolName();
        String schoolAddress = school.getAddress();
        String schoolMission = school.getMission();
        String schoolVision = school.getVision();
        String schoolGender = school.getGender();
        Integer schoolCutOff = school.getCutOffPoint();
        ArrayList<String> schoolCCA = school.getCca();
        String subjects = school.getSubjects();
        ArrayList<String> schoolSubject = new ArrayList<String>();
        schoolSubject.add(subjects);
        ArrayList<String> schoolContact = school.getContactInfo();
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

        schName.setText(schoolName);
        schVision.setText("Vision:" + "\n" + schoolVision);
        schMission.setText("Mission:" + "\n" + schoolMission);
        schCutOff.setText("Cut-Off Point: " + schoolCutOff.toString());
        schGender.setText("School Type: " + schoolGender.toLowerCase());
        Glide.with(this).load(imageUrl).error(R.drawable.ic_person).into(schLogo);
    }
}