package com.example.myapplication;

public class Comment {

    private String usercomment;
    private String username;
    private int imageNo;
    private String cid;

    public Comment(String usercomment, String username, int imageNo) {
        this.usercomment = usercomment;
        this.username = username;
        this.imageNo = imageNo;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Comment() {
    }


    public String getUsercomment() {
        return usercomment;
    }

    public void setUsercomment(String usercomment) {
        this.usercomment = usercomment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getImageNo() {
        return imageNo;
    }

    public void setImageNo(int imageNo) {
        this.imageNo = imageNo;
    }
}
