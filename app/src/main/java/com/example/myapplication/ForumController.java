
package com.example.myapplication;

import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ForumController {


    public static boolean postbuttonmethod(EditText posttitle, EditText postcontent, String name){

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
            Post post = new Post(title, content, name);
            root = db.getReference("Posts").push();
            String postKey = root.getKey();
            post.setPostKey(postKey);

            root.setValue(post);
            return true;

        }

    }

    public static void deleteButtonmethod(String cid, String postKey){

        FirebaseDatabase db = FirebaseDatabase.getInstance();

        DatabaseReference root = db.getReference("Comment").child(postKey).child(cid);
        root.removeValue();



    }

    public static void deletepostmethod(String postKey){

        FirebaseDatabase db = FirebaseDatabase.getInstance();

        DatabaseReference root = db.getReference("Posts").child(postKey);
        root.removeValue();



    }

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


    public static int CApostcommentbuttonmethod(EditText edittextpostcomment, String postKey, String name, int imageNo){


        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference root;


        if (edittextpostcomment.length()== 0)
        {
            return 2;
        }
        else if (edittextpostcomment.length()>30)
        {
            return 1;
        }

        else
        {


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


