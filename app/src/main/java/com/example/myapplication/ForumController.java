
package com.example.myapplication;

import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Represents the ForumController which controls the application logic for the forum section
 */
public class ForumController {

    /**
     * Creates a new post
     * @param posttitle The title of the post
     * @param postcontent The content of the post
     * @param name The name of the user who created the post
     * @param email The email of the user who created the post
     * @return boolean to indicate success or failure of the creation
     */
    public static boolean postbuttonmethod(EditText posttitle, EditText postcontent, String name, String email){

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference root;
        if (posttitle.length()== 0)
        {
            return false;
        }
        else
        {
            String title = posttitle.getText().toString();
            String content = postcontent.getText().toString();
            Post post = new Post(title, content, name, email);
            root = db.getReference("Posts").push();
            String postKey = root.getKey();
            post.setPostKey(postKey);

            root.setValue(post);
            return true;
        }
    }

    /**
     * Delete a user's comment in the Firebase
     * @param cid The ID of the comment
     * @param postKey The key of the post
     */
    public static void deleteButtonmethod(String cid, String postKey){

        FirebaseDatabase db = FirebaseDatabase.getInstance();

        DatabaseReference root = db.getReference("Comment").child(postKey).child(cid);
        root.removeValue();
    }

    /**
     * Delete a user's post in the Firebase
     * @param postKey The key of the post
     */
    public static void deletepostmethod(String postKey){

        FirebaseDatabase db = FirebaseDatabase.getInstance();

        DatabaseReference root = db.getReference("Posts").child(postKey);
        root.removeValue();
        db.getReference("Comment").child(postKey).removeValue();
    }

    /**
     * Checks if the name of the current user matches the name of the post's creator
     * @param postUsername The name of the user who created the post
     * @param name The name of the current user
     * @return boolean to indicate whether the names match
     */
    public static boolean checkPost(String postUsername, String name)
    {
        if (postUsername.equals(name))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Checks if the name of the current user matches the name of the comment's creator
     * @param comment The name of the user who made the comment
     * @param name The name of the current user
     * @return Whether the names match
     */
    public static boolean checkComment(Comment comment, String name)
    {
        if (comment.getUsername().equals(name))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Creates a new comment
     * @param edittextpostcomment The content of the comment
     * @param postKey The key of the post
     * @param name The name of the user who made the comment
     * @param imageNo The image number of the user who made the comment
     * @return integer to indicate status of creation
     */
    public static int CApostcommentbuttonmethod(EditText edittextpostcomment, String postKey, String name, int imageNo){

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference root;

        if (edittextpostcomment.length()<= 5)
        {
            return 2;
        }
        else if (edittextpostcomment.length()>100)
        {
            return 1;
        }
        else {
            String comment_content = edittextpostcomment.getText().toString();
            Comment comment = new Comment(comment_content, name, imageNo);
            root = db.getReference("Comment").child(postKey).push();

            //create id for the comment
            String cid = root.getKey();
            comment.setCid(cid);
            root.setValue(comment);
            return 0;
        }
    }
}


