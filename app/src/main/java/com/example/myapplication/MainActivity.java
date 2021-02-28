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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView register, forgotPassword;
    private EditText logEmail, logPassword;
    private Button login;

    private FirebaseAuth mAuth;
    private ProgressBar logProgress;
//     dsadsadas
    private String test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);

        logEmail = (EditText) findViewById(R.id.logEmail);
        logPassword = (EditText) findViewById(R.id.logPassword);

        logProgress = (ProgressBar) findViewById(R.id.logProgress);

        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forgotPassword:
                startActivity(new Intent(this, ForgotPassword.class));
                break;
            case R.id.register:
                startActivity(new Intent(this, RegisterUser.class));
                break;
            case R.id.login:
                userLogin();
                break;
        }
    }

    private void userLogin() {

        String email = logEmail.getText().toString().trim();
        String password = logPassword.getText().toString().trim();

        if(email.isEmpty()){
            logEmail.setError("Email is not entered!");
            logEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            logEmail.setError("Please enter a valid email address!");
            logEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            logPassword.setError("Password is not entered!");
            logPassword.requestFocus();
        }

        if(password.length() < 6){
            logPassword.setError("Password should be at least 6 characters!");
            logPassword.requestFocus();
            return;
        }

        logProgress.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()) {
                        startActivity(new Intent(MainActivity.this, Menu.class));
                        logProgress.setVisibility(View.GONE);
                    }
                    else{
                        Snackbar.make(getCurrentFocus(), "Please verify your email.", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Resend", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        user.sendEmailVerification();
                                    }
                                }).show();
                        logProgress.setVisibility(View.GONE);
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "Failed to login! Please check your email and password again.", Toast.LENGTH_LONG).show();
                    logProgress.setVisibility(View.GONE);
                }
            }
        });
    }
}