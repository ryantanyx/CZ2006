package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Represents the ForgotPassword Activity when user clicks on the button
 */
public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {
    /**
     * Firebase authenticator
     */
    FirebaseAuth auth;
    /**
     * EditText for user to enter their email
     */
    private EditText forgotEmail;
    /**
     * Reset password button
     */
    private Button reset;
    /**
     * ProgressBar to indicate progress
     */
    private ProgressBar forgotProgress;
    /**
     * TextView to display the relevant text
     */
    private TextView forgotLogin;
    /**
     * Creation of activity from savedInstanceState and setting the layout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        forgotEmail = (EditText) findViewById(R.id.forgotEmail);
        reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(this);
        forgotProgress = (ProgressBar) findViewById(R.id.forgotProgress);
        forgotLogin = (TextView) findViewById(R.id.forgotLogin);
        forgotLogin.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Sends email to user for password reset if fields entered are valid
     */
    private void resetPassword(){
        String email = forgotEmail.getText().toString().trim();

        if(email.isEmpty()){
            forgotEmail.setError("Please enter your email address!");
            forgotEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            forgotEmail.setError("Please provide a valid email address!");
            forgotEmail.requestFocus();
            return;
        }

        forgotProgress.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    forgotProgress.setVisibility(View.GONE);
                    Snackbar.make(getCurrentFocus(), "Check your email to reset your password", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).show();
                }
                else{
                    forgotProgress.setVisibility(View.GONE);
                    Snackbar.make(getCurrentFocus(), "Try again! Something wrong happened!", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).show();
                }
            }
        });
    }
    /**
     * Switch case to execute different commands for the respective buttons
     * @param v view
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.reset:
                resetPassword();
                break;
            case R.id.forgotLogin:
                startActivity(new Intent(this, LoginUser.class));
                break;
        }
    }
}