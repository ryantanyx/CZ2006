package com.example.myapplication;

/**
 * Represents the Comment Entity in the Forum segment
 */
public class Comment {
    /**
     * Comment made by this user
     */
    private String usercomment;
    /**
     * Name of the user who made this comment
     */
    private String username;
    /**
     * Image Number of the user who made this comment
     */
    private int imageNo;
    /**
     * Comment ID of this comment
     */
    private String cid;

    /**
     * Constructor to create a new comment
     * @param usercomment the comment made by the user
     * @param username the name of the user who made this comment
     * @param imageNo the image number of the user who made this comment
     */
    public Comment(String usercomment, String username, int imageNo) {
        this.usercomment = usercomment;
        this.username = username;
        this.imageNo = imageNo;
    }

    /**
     * Get this comment's ID
     * @return this comment's ID
     */
    public String getCid() {
        return cid;
    }

    /**
     * Set this comment's ID
     * @param cid
     */
    public void setCid(String cid) {
        this.cid = cid;
    }

    /**
     * Empty constructor
     */
    public Comment() {
    }

    /**
     * Get the comment made
     * @return the comment made
     */
    public String getUsercomment() {
        return usercomment;
    }

    /**
     * Set the comment made
     * @param usercomment
     */
    public void setUsercomment(String usercomment) {
        this.usercomment = usercomment;
    }

    /**
     * Get the name of the user who made this comment
     * @return name of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the name of the user who made this comment
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the image number of the user who made this comment
     * @return the image number of the user who made this comment
     */
    public int getImageNo() {
        return imageNo;
    }

    /**
     * Set the image number of the user who made this comment
     * @param imageNo
     */
    public void setImageNo(int imageNo) {
        this.imageNo = imageNo;
    }
}
