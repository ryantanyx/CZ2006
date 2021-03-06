package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents the School Entity for users to find information on
 */
public class School implements Parcelable {
    /**
     * Creator that instantiates the class from the given Parcel
     */
    public static final Creator<School> CREATOR = new Creator<School>() {
        @Override
        public School createFromParcel(Parcel in) {
            return new School(in);
        }

        @Override
        public School[] newArray(int size) {
            return new School[size];
        }
    };
    /**
     * HashMap<Sring, ArrayList<String> to store the type of CCA and the list of CCAs belonging to that type
     */
    HashMap<String, ArrayList<String>> cca;
    /**
     * Strings to store the URL of image, name of school, address, mission, vission, gender, location, region, type, and subjects of the school
     */
    private String imageUrl,schoolName, address,
            mission, vision, gender, location,
            region, type, subjects;
    /**
     * ArrayList<String> to store the contact and transport information of the school
     */
    private ArrayList<String> contactInfo, transport;
    /**
     *     HashMap <String, Integer> store the type of stream and the respective PSLE cutoff for that stream.
     */
    private HashMap<String, Integer> cutOffPoint;

    /**
     * Empty Constructor
     */
    public School() {
    }

    /**
     * School constructor to create a new school based on the Parcel
     * @param in The parcel
     */
    protected School(Parcel in) {
        imageUrl = in.readString();
        schoolName = in.readString();
        address = in.readString();
        mission = in.readString();
        vision = in.readString();
        gender = in.readString();
        location = in.readString();
        region = in.readString();
        type = in.readString();
        cutOffPoint = in.readHashMap(null);
        cca = in.readHashMap(null);
        subjects = in.readString();
        contactInfo = in.readArrayList(null);
        transport = in.readArrayList(null);
    }

    /**
     * Get the mission of this school
     * @return this school's mission
     */
    public String getMission() {
        return mission;
    }

    /**
     * Set the mission of this school
     * @param mission This school's new mission
     */
    public void setMission(String mission) {
        this.mission = mission;
    }

    /**
     * Get the vision of this school
     * @return This school's vision
     */
    public String getVision() {
        return vision;
    }

    /**
     * Set the vision of this school
     * @param vision This school's new vision
     */
    public void setVision(String vision) {
        this.vision = vision;
    }

    /**
     * Get the URL of the image of this school
     * @return The URL of the image
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Set the URL for the image of this school
     * @param imageUrl The image's new URL
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Get the name of this school
     * @return This school's name
     */
    public String getSchoolName() {
        return schoolName;
    }

    /**
     * Set the name of this school
     * @param schoolName The school's new name
     */
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    /**
     * Get the address of this school
     * @return The address of this school
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set the address of this school
     * @param address This school's new address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Get the gender status of this school (single-sex vs co-ed)
     * @return The gender status of this school
     */
    public String getGender() {
        return gender;
    }

    /**
     * Set the gender status of this school
     * @param gender The school's new gender status
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Get the location of this school
     * @return This school's location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Set the location of this school
     * @param location This school's new location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Get the region where this school is located in
     * @return The region of this school
     */
    public String getRegion() {
        return region;
    }

    /**
     * Set the region where this school is located in
     * @param region This school's new region
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Get the type of this school (Government vs Independent)
     * @return The type of this school
     */
    public String getType() {
        return type;
    }

    /**
     * Set the type of this school
     * @param type This school's new type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get the HashMap of PSLE cut-off points for this school
     * @return the HashMap of PSLE cut-off points for this school
     */
    public HashMap<String, Integer> getCutOffPoint() {
        return cutOffPoint;
    }

    /**
     * Set the HashMap of PSLE cut-off points for this school
     * @param cutOffPoint This school's new PSLE cut-off points
     */
    public void setCutOffPoint(HashMap<String, Integer> cutOffPoint) {
        this.cutOffPoint = cutOffPoint;
    }

    /**
     * Get the HashMap of the CCAs offered by this school
     * @return This school's CCAs offered
     */
    public HashMap<String, ArrayList<String>> getCca() {
        return cca;
    }

    /**
     * Set the HashMap of the CCAs offered by this school
     * @param cca This school's new CCAs offered
     */
    public void setCca(HashMap<String, ArrayList<String>> cca) {
        this.cca = cca;
    }

    /**
     * Get the list of subjects in String format offered by this school
     * @return This school's list of subjects offered
     */
    public String getSubjects() {
        return subjects;
    }

    /**
     * Set the subjects offered by this school
     * @param subjects This school's new list of subjects offered
     */
    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    /**
     * Get the contact information of this school
     * @return The contact information of this school
     */
    public ArrayList<String> getContactInfo() {
        return contactInfo;
    }

    /**
     * Set the contact information of this school
     * @param contactInfo This school's new contact information
     */
    public void setContactInfo(ArrayList<String> contactInfo) {
        this.contactInfo = contactInfo;
    }

    /**
     * Get the transport information of this school
     * @return This school's transport information
     */
    public ArrayList<String> getTransport() {
        return transport;
    }

    /**
     * Set the transport information of this school
     * @param transport This school's new transport information
     */
    public void setTransport(ArrayList<String> transport) {
        this.transport = transport;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes the information of the school to a parcel
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeString(schoolName);
        dest.writeString(address);
        dest.writeString(mission);
        dest.writeString(vision);
        dest.writeString(gender);
        dest.writeString(location);
        dest.writeString(region);
        dest.writeString(type);
        dest.writeMap(cutOffPoint);
        dest.writeMap(cca);
        dest.writeString(subjects);
        dest.writeList(contactInfo);
        dest.writeList(transport);
    }
}