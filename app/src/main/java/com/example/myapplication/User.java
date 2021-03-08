package com.example.myapplication;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class User {

    private String name, gender, date;
    private int imageNo;

    private ArrayList<School> favList = new ArrayList<School>();
    public User(){

    }

    public User(String name, String gender, String date, int imageNo, ArrayList<School> favList) {
        this.name = name;
        this.gender = gender;
        this.date = date;
        this.imageNo = imageNo;
        this.favList = favList;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getImageNo() {
        return imageNo;
    }

    public void setImageNo(int imageNo) {
        this.imageNo = imageNo;
    }

    public ArrayList<School> getFavList() {
        return this.favList;
    }

    public void setFavList(ArrayList<School> favList) {
        this.favList = favList;
    }
}
