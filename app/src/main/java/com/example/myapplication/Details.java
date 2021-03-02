package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class Details extends AppCompatActivity {

    TextView schName, schVision, schMission;
    ImageView schLogo;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        schName = findViewById(R.id.schName);
        schLogo = findViewById(R.id.schLogo);
        schVision = findViewById(R.id.schVision);
        schMission = findViewById(R.id.schMission);
        //TODO: programme and cca expandable adaptor

        Intent i = getIntent();
        School school = i.getParcelableExtra("School");

        String imageUrl = school.getImageUrl();
        String schoolName = school.getSchoolName();
        String schoolAddress = school.getAddress();
        String schoolMission = school.getMission();
        String schoolVision = school.getVision();

        schName.setText(schoolName);
        schVision.setText("Vision:" + "\n" + schoolVision);
        schMission.setText("Mission:" + "\n" +schoolMission);
        Glide.with(this).load(imageUrl).error(R.drawable.ic_person).into(schLogo);

    }
}