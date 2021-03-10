package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Comparison extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: Create comparison layout
        setContentView(R.layout.activity_details);

        Intent i = getIntent();
        ArrayList<School> school = i.getParcelableArrayListExtra("Selected");
        System.out.println(school.toString());

        //TODO: Fill in all the stuff onto the page
    }
}
