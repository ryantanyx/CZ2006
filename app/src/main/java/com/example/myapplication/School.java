package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class School implements Parcelable {

    private String imageUrl,schoolName, address,
            mission, vision, gender, location,
            region, type, subjects;
    private ArrayList<String> cca, contactInfo, transport;
    private Integer cutOffPoint;

    public School() {
    }

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
        cutOffPoint = in.readInt();
        cca = in.readArrayList(null);
        subjects = in.readString();
        contactInfo = in.readArrayList(null);
        transport = in.readArrayList(null);
    }

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

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public String getVision() {
        return vision;
    }

    public void setVision(String vision) {
        this.vision = vision;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCutOffPoint() {
        return cutOffPoint;
    }

    public void setCutOffPoint(int cutOffPoint) {
        this.cutOffPoint = cutOffPoint;
    }

    public ArrayList<String> getCca() {
        return cca;
    }

    public void setCca(ArrayList<String> cca) {
        this.cca = cca;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public ArrayList<String> getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ArrayList<String> contactInfo) {
        this.contactInfo = contactInfo;
    }

    public ArrayList<String> getTransport() {
        return transport;
    }

    public void setTransport(ArrayList<String> transport) {
        this.transport = transport;
    }

    @Override
    public int describeContents() {
        return 0;
    }

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
        dest.writeInt(cutOffPoint);
        dest.writeList(cca);
        dest.writeString(subjects);
        dest.writeList(contactInfo);
        dest.writeList(transport);
    }
}
