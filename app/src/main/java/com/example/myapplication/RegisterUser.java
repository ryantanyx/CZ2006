package com.example.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Represents the User Registration Activity Boundary where users register for an account
 */
public class RegisterUser extends AppCompatActivity implements View.OnClickListener{
    /**
     * Firebase Authenticator
     */
    private FirebaseAuth mAuth;
    /**
     * DatePickerDialog for users to select their birth date
     */
    private DatePickerDialog picker;
    /**
     * AlertDialog for confirmation of registration
     */
    private AlertDialog dialog;
    /**
     * AlertDialog builder to create dialog
     */
    private AlertDialog.Builder builder;
    /**
     * EditText for users to enter their name, email, address, password, confirmed password, and birth date
     */
    private EditText regDate, regName, regEmail, regAddress, regPassword, regReenterPassword;
    /**
     * Buttons for user to register, cancel, and to choose a profile picture
     */
    private Button regUser, regCancel, chooseImage;
    /**
     * ProgressBar to show registration progress
     */
    private ProgressBar regProgress;
    /**
     * CheckBox for users to accept disclaimer
     */
    private CheckBox regAgree;
    /**
     * RadioGroup for users to choose gender
     */
    private RadioGroup regGender;
    /**
     * RadioButton to indicate what the user has selected
     */
    private RadioButton selectedRadioButton;
    /**
     * Dialog to show success message and profile image options
     */
    private Dialog dialog2, dialog3;
    /**
     * CardView to display the profile image background
     */
    private CardView regImagebg;
    /**
     * ImageView to display the profile image of the user
     */
    private ImageView regImage;
    /**
     * regImageNo to store the image chosen by the user. Default is set to 1
     */
    private int regImageno = 1;
    /**
     * Creation of activity from savedInstanceState and setting the layout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialog2 = new Dialog(RegisterUser.this);
        dialog2.setContentView(R.layout.custom_dialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog2.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
        }
        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog2.setCancelable(false);
        dialog2.getWindow().getAttributes().windowAnimations = R.style.animation;

        TextView okay = (TextView) dialog2.findViewById(R.id.okay);
        okay.setOnClickListener(this);

        dialog3 = new Dialog(RegisterUser.this);
        dialog3.setContentView(R.layout.image_picker);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog3.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
        }
        dialog3.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog3.getWindow().getAttributes().windowAnimations = R.style.animation;

        CardView image1 = (CardView) dialog3.findViewById(R.id.image1);
        image1.setOnClickListener(this);
        CardView image2 = (CardView) dialog3.findViewById(R.id.image2);
        image2.setOnClickListener(this);
        CardView image3 = (CardView) dialog3.findViewById(R.id.image3);
        image3.setOnClickListener(this);
        CardView image4 = (CardView) dialog3.findViewById(R.id.image4);
        image4.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        regDate=(EditText) findViewById(R.id.regDate);
        regDate.setInputType(InputType.TYPE_NULL);
        regDate.setOnClickListener(this);
        regImagebg = (CardView) findViewById(R.id.regImagebg);
        regImage = regImagebg.findViewById(R.id.regImage);

        regUser = (Button) findViewById(R.id.regUser);
        regUser.setOnClickListener(this);
        regCancel = (Button) findViewById(R.id.regBack);
        regCancel.setOnClickListener(this);
        chooseImage = (Button) findViewById(R.id.chooseImage);
        chooseImage.setOnClickListener(this);

        regName = (EditText) findViewById(R.id.regName);
        regEmail = (EditText) findViewById(R.id.regEmail);
        regAddress = (EditText) findViewById(R.id.regAddress);
        regPassword = (EditText) findViewById(R.id.regPassword);
        regReenterPassword = (EditText) findViewById(R.id.regReenterPassword);

        regProgress = (ProgressBar) findViewById(R.id.regProgress);
        regAgree = (CheckBox) findViewById(R.id.regAgree);
        regGender = (RadioGroup) findViewById(R.id.regGender);
    }
    /**
     * Switch case to execute different commands for the respective buttons
     * @param v view
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.regBack:
                startActivity(new Intent(this, LoginUser.class));
                break;
            case R.id.regUser:
                builder = new AlertDialog.Builder(RegisterUser.this);
                builder.setTitle("Are you sure you want to proceed with registration?");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        registerUser(v);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog = builder.create();
                dialog.show();
                break;
            case R.id.regDate:
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(RegisterUser.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                regDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
                break;
            case R.id.okay:
                dialog2.dismiss();
                break;
            case R.id.chooseImage:
                dialog3.show();
                break;
            case R.id.image1:
                regImageno = 1;
                dialog3.dismiss();
                regImage.setImageResource(R.drawable.image1);
                break;
            case R.id.image2:
                regImageno = 2;
                dialog3.dismiss();
                regImage.setImageResource(R.drawable.image2);
                break;
            case R.id.image3:
                regImageno = 3;
                dialog3.dismiss();
                regImage.setImageResource(R.drawable.image3);
                break;
            case R.id.image4:
                regImageno = 4;
                dialog3.dismiss();
                regImage.setImageResource(R.drawable.image4);
                break;
        }
    }
    /**
     * Checks user's registration details and registers the user account or display relevant error messages
     */
    public void registerUser(View v){

        String name = regName.getText().toString().trim();
        String email = regEmail.getText().toString().trim();
        String address = regAddress.getText().toString().trim();
        String password = regPassword.getText().toString().trim();
        String reenterPassword = regReenterPassword.getText().toString().trim();
        String gender;
        String date = regDate.getText().toString().trim();
        ArrayList<School> favList = new ArrayList<School>();

        if(name.isEmpty()){
            regName.setError("Full name is not entered!");
            regName.requestFocus();
            return;
        }

        if(email.isEmpty()){
            regEmail.setError("Email is not entered!");
            regEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            regEmail.setError("Please provide a valid email address!");
            regEmail.requestFocus();
            return;
        }

        if(address.isEmpty()){
            regAddress.setError("Address is not entered!");
            regAddress.requestFocus();
            return;
        }

        /*if(!MapController.isValidAddress(v.getContext(), address)){
            regAddress.setError("Please provide a valid home address!");
            regAddress.requestFocus();
            return;
        }*/

        if(password.isEmpty()){
            regPassword.setError("Password is not entered!");
            regPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            regPassword.setError("Password should be at least 6 characters!");
            regPassword.requestFocus();
            return;
        }

        if(!password.equals(reenterPassword)){
            regReenterPassword.setError("Passwords do not match!");
            regReenterPassword.requestFocus();
            return;
        }

        if(regGender.getCheckedRadioButtonId()==-1){
            Toast.makeText(this, "Gender is not selected!", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            int selectedID = regGender.getCheckedRadioButtonId();
            selectedRadioButton = (RadioButton)findViewById(selectedID);
            gender = selectedRadioButton.getText().toString();
        }

        if(date.isEmpty()){
            regDate.setError("Date of birth is not entered!");
            regDate.requestFocus();
            return;
        }

        if(!regAgree.isChecked()){
            Toast.makeText(this, "Please acknowledge the disclaimer!", Toast.LENGTH_SHORT).show();
            return;
        }

        regProgress.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            User myUser = new User(name, address, gender, date, regImageno, favList);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(myUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                                        dialog2.show();
                                        regProgress.setVisibility(View.GONE);
                                    }
                                    else{
                                        Snackbar.make(getCurrentFocus(), "User failed to register.", Snackbar.LENGTH_INDEFINITE)
                                                .setAction("Dismiss", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                    }
                                                }).show();
                                        regProgress.setVisibility(View.GONE);
                                    }
                                }
                            });

                        }else{
                            Snackbar.make(getCurrentFocus(), "User failed to register", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Dismiss", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    }).show();
                            regProgress.setVisibility(View.GONE);
                        }
                    }
                });
    }
}