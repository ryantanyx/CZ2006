package com.example.myapplication;

public class Post {


    private String title, content, postKey;


    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }


    public Post() {
    }


    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }



}
