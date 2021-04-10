package com.example.myapplication;

import java.util.ArrayList;
/**
 * Represents the User Entity
 */
public class User {
    /**
     * Name, address, gender and birth date of this user stored in String format
     */
    private String name, address, gender, date;
    /**
     * Integer to represent the profile picture this user has selected
     */
    private int imageNo;
    /**
     * The list of schools stored in the favourite list of this user
     */
    private ArrayList<School> favList;
    /**
     * Required public empty constructor
     */
    public User(){

    }

    /**
     * Constructor to create a new user
     * @param name The name of this user
     * @param address The address of this user
     * @param gender The gender of this user
     * @param date The birth date of this user
     * @param imageNo The profile picture number of this user
     * @param favList The favourite list of this user
     */
    public User(String name, String address, String gender, String date, int imageNo, ArrayList<School> favList) {
        this.name = name;
        this.address = address;
        this.gender = gender;
        this.date = date;
        this.imageNo = imageNo;
        this.favList = favList;
    }

    /**
     * Get the name of this user
     * @return This user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of this user
     * @param name This user's new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the address of this user
     * @return This user's address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set the address of this user
     * @param address This user's new address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Get the gender of this user
     * @return This user's gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Set the gender of this user
     * @param gender This user's new gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Get the birth date of this user
     * @return This user's birth date
     */
    public String getDate() {
        return date;
    }

    /**
     * Set the birth date of this user
     * @param date This user's new birth date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Get the image number of this user
     * @return This user's image number
     */
    public int getImageNo() {
        return imageNo;
    }

    /**
     * Set the image number of this user
     * @param imageNo This user's new image number
     */
    public void setImageNo(int imageNo) {
        this.imageNo = imageNo;
    }

    /**
     * Get the list of favourite schools of this user
     * @return This user's list of favourite schools
     */
    public ArrayList<School> getFavList() {
        return this.favList;
    }

    /**
     * Set the list of favourite schools of this user
     * @param favList This user's new list of favourite schools
     */
    public void setFavList(ArrayList<School> favList) {
        this.favList = favList;
    }
}
