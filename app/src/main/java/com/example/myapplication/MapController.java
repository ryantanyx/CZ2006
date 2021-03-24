package com.example.myapplication;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapController {

    public static HashMap<String, LatLng> getLatLong(Context context, List<School> schoolList){
        HashMap<String, LatLng> nearbySch = new HashMap<>();
        for (School sch : schoolList){
            LatLng latLng = getLocationFromAddress(context, sch.getAddress());
            // Filtering schools based on distance
            nearbySch.put(sch.getSchoolName(), latLng);
            //if (distance(userPosition.latitude, userPosition.longitude, latLng.latitude, latLng.longitude) <= 5){

            //}
        }

        return nearbySch;
    };

    private static double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 6371; // in miles, change to 6371 for kilometer output

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double dist = earthRadius * c;

        return dist; // output distance, in MILES
    }

    public static LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    public static HashMap<String, LatLng> getLatLong(Context context, HashMap<String, LatLng> latLongList, LatLng userPosition) {
        HashMap<String, LatLng> nearbySch = new HashMap<>();
        for (Map.Entry<String, LatLng> entry : latLongList.entrySet()){
            // Filtering schools based on distance
            if (distance(userPosition.latitude, userPosition.longitude, entry.getValue().latitude, entry.getValue().longitude) <= 5){
                nearbySch.put(entry.getKey(), entry.getValue());
            }
        }
        return nearbySch;
    }
}
