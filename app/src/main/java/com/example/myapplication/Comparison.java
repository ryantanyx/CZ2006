package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Comparison extends AppCompatActivity implements View.OnClickListener{

    ImageView schLogo, backbutton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: Create comparison layout
        setContentView(R.layout.activity_details);

        Intent i = getIntent();
        ArrayList<School> school = i.getParcelableArrayListExtra("Selected");
        System.out.println(school.toString());

        backbutton =  (ImageView) findViewById(R.id.detailsBackButton);
        backbutton.setOnClickListener(this);

        //TODO: Fill in all the stuff onto the page
    }

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
