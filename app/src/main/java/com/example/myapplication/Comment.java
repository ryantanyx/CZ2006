package com.example.myapplication;

public class Comment {

    private String usercomment;

    public Comment(String usercomment) {
        this.usercomment = usercomment;
    }

    public Comment() {
    }


    public String getUsercomment() {
        return usercomment;
    }

    public void setUsercomment(String usercomment) {
        this.usercomment = usercomment;
    }
}
