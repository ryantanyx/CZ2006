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

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    private EditText forgotEmail;
    private Button reset;
    private ProgressBar forgotProgress;
    private TextView forgotLogin;

    FirebaseAuth auth;

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