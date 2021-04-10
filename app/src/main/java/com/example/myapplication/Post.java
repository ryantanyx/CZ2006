package com.example.myapplication;

/**
 * Represents the Post Entity in the Forum segment
 */
public class Post {

    /**
     * Strings to store the post title, content, id, name of user, and email of user
     */
    private String title, content, postKey, username, email;


    /**
     * Constructor to create a new post
     * @param title title of this post
     * @param content content of this post
     * @param username name of user who created this post
     * @param email email of user who created this post
     */
    public Post(String title, String content, String username, String email) {
        this.title = title;
        this.content = content;
        this.username = username;
        this.email = email;
    }

    /**
     * Empty constructor
     */
    public Post() {
    }

    /**
     * Get the name of the user who created this post
     * @return name of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the name of the user who created this post
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the key/id of this post
     * @return this post's key
     */
    public String getPostKey() {
        return postKey;
    }

    /**
     * Set the key/id of this post
     * @param postKey
     */
    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    /**
     * Get the title of this post
     * @return this post's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the content of this post
     * @return the post's content
     */
    public String getContent() {
        return content;
    }

    /**
     * Set the title of this post
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * Set the content of this post
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Get the email of the user who created this post
     * @return this post's user's email
     */
    public String getEmail() {
        return email;
    }
    /**
     * Set the email of the user who created this post
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

}
