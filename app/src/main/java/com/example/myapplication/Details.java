package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class Details extends AppCompatActivity {

    TextView detailsTitle;
    ImageView detailsImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        detailsTitle = findViewById(R.id.detailsTitle);
        detailsImage = findViewById(R.id.detailsImage);

        Intent i = getIntent();
        School school = i.getParcelableExtra("School");

        String imageUrl = school.getImageUrl();
        String schoolName = school.getSchoolName();
        String schoolAddress = school.getAddress();

        detailsTitle.setText(schoolName + "\n" + schoolAddress);
        Glide.with(this).load(imageUrl).error(R.drawable.ic_person).into(detailsImage);

    }
}