package com.example.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment implements View.OnClickListener {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    private Button logout, updateProfile, changePassword;
    private CardView profileImgbg;
    private ImageView profileImg;
    private EditText profileName, profileEmail, profileDate;
    private EditText currentPassword, newPassword, rePassword;
    private ImageView profileGender, edit;
    private int profileImageno;

    private DatePickerDialog picker;
    private Dialog dialog2, dialog3;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        getActivity().setTitle("Account");

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        profileImgbg = (CardView) view.findViewById(R.id.profileImgbg);
        profileImg = (ImageView) profileImgbg.findViewById(R.id.profileImg);
        profileName = (EditText) view.findViewById(R.id.profileName);
        profileEmail = (EditText) view.findViewById(R.id.profileEmail);
        profileGender = (ImageView) view.findViewById(R.id.profileGender);
        profileDate = (EditText) view.findViewById(R.id.profileDate);
        profileDate.setInputType(InputType.TYPE_NULL);
        profileDate.setOnClickListener(this);
        edit = (ImageView) view.findViewById(R.id.edit);
        edit.setOnClickListener(this);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null){
                    int imageNo = userProfile.imageNo;
                    String name = userProfile.name;
                    String email = user.getEmail();
                    String gender = userProfile.gender;
                    String date = userProfile.date;

                    if (imageNo == 1){
                        profileImg.setImageResource(R.drawable.image1);
                    }
                    else if (imageNo == 2){
                        profileImg.setImageResource(R.drawable.image2);
                    }
                    else if (imageNo == 3){
                        profileImg.setImageResource(R.drawable.image3);
                    }
                    else if (imageNo == 4){
                        profileImg.setImageResource(R.drawable.image4);
                    }
                    profileName.setText(name);
                    profileName.setSelection(name.length());
                    profileEmail.setText(email);
                    profileEmail.setSelection(email.length());

                    if (gender.equals("Male")){
                        profileGender.setImageResource(R.drawable.ic_male);
                    }
                    else if (gender.equals("Female")){
                        profileGender.setImageResource(R.drawable.ic_female);
                    }

                    profileDate.setText(date);
                    profileDate.setSelection(date.length());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });

        updateProfile = (Button) view.findViewById(R.id.updateProfile);
        updateProfile.setOnClickListener(this);
        changePassword = (Button) view.findViewById(R.id.changePassword);
        changePassword.setOnClickListener(this);
        logout = (Button) view.findViewById(R.id.logout);
        logout.setOnClickListener(this);

        dialog2 = new Dialog(getActivity());
        dialog2.setContentView(R.layout.change_password);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog2.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.background));
        }
        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog2.setCancelable(false);
        dialog2.getWindow().getAttributes().windowAnimations = R.style.animation;

        dialog3 = new Dialog(getActivity());
        dialog3.setContentView(R.layout.image_picker);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog3.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.background));
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

        currentPassword = (EditText) dialog2.findViewById(R.id.currentPassword);
        newPassword = (EditText) dialog2.findViewById(R.id.newPassword);
        rePassword = (EditText) dialog2.findViewById(R.id.rePassword);

        Button cancel = (Button) dialog2.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        Button confirm = (Button) dialog2.findViewById(R.id.confirm);
        confirm.setOnClickListener(this);

        return view;
    }

    public void onClick(View view){
        switch(view.getId()){
            case R.id.edit:
                dialog3.show();
                break;
            case R.id.profileDate:
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                profileDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
                break;
            case R.id.updateProfile:
                builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Are you sure you want to update your profile?");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateProfile();
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

            case R.id.changePassword:
                dialog2.show();
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), MainActivity.class));
                break;
            case R.id.cancel:
                dialog2.dismiss();
                break;
            case R.id.confirm:
                changePassword();
                break;
            case R.id.image1:
                profileImageno = 1;
                dialog3.dismiss();
                profileImg.setImageResource(R.drawable.image1);
                break;
            case R.id.image2:
                profileImageno = 2;
                dialog3.dismiss();
                profileImg.setImageResource(R.drawable.image2);
                break;
            case R.id.image3:
                profileImageno = 3;
                dialog3.dismiss();
                profileImg.setImageResource(R.drawable.image3);
                break;
            case R.id.image4:
                profileImageno = 4;
                dialog3.dismiss();
                profileImg.setImageResource(R.drawable.image4);
                break;
        }
    }

    public void updateProfile() {

        String name = profileName.getText().toString();
        String email = profileEmail.getText().toString();
        String date = profileDate.getText().toString();

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        if(name.isEmpty()){
            profileName.setError("Full name cannot be 0 characters!");
            profileName.requestFocus();
            return;
        }

        if(email.isEmpty()){
            profileEmail.setError("Email cannot be 0 characters!");
            profileEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            profileEmail.setError("Please provide a valid email address!");
            profileEmail.requestFocus();
            return;
        }

        if(date.isEmpty()){
            profileDate.setError("Date of Birth cannot be 0 characters!");
            profileDate.requestFocus();
            return;
        }

        reference.child(userID).child("name").setValue(name);
        user.updateEmail(email);
        reference.child(userID).child("date").setValue(date);
        reference.child(userID).child("imageNo").setValue(profileImageno);
        Toast.makeText(getActivity(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
    }

    public void changePassword() {

        String currentPw = currentPassword.getText().toString();
        String newPw = newPassword.getText().toString();
        String rePw = rePassword.getText().toString();

        if(currentPw.isEmpty()){
            currentPassword.setError("Please enter your current password!");
            currentPassword.requestFocus();
            return;
        }

        if(newPw.isEmpty()){
            newPassword.setError("Please enter your new password!");
            newPassword.requestFocus();
            return;
        }

        if(rePw.isEmpty()){
            rePassword.setError("Please re-enter your new password!");
            rePassword.requestFocus();
            return;
        }

        if(newPw.length() < 6){
            newPassword.setError("Password should be at least 6 characters!");
            newPassword.requestFocus();
            return;
        }

        if(!newPw.equals(rePw)){
            rePassword.setError("Passwords do not match!");
            rePassword.requestFocus();
            return;
        }

        user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(),currentPw);

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    user.updatePassword(newPw).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                currentPassword.setText("");
                                newPassword.setText("");
                                rePassword.setText("");
                                dialog2.dismiss();
                                Toast.makeText(getActivity(), "Password Updated Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                currentPassword.setText("");
                                newPassword.setText("");
                                rePassword.setText("");
                                dialog2.dismiss();
                                Toast.makeText(getActivity(), "Failed To Update Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    currentPassword.setText("");
                    newPassword.setText("");
                    rePassword.setText("");
                    dialog2.dismiss();
                    Toast.makeText(getActivity(), "Current password does not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}