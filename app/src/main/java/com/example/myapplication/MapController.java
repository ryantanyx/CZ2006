package com.example.myapplication;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.List;

public class MapController {

    public static HashMap<String, LatLng> getLatLong(List<School> schoolList, LatLng userPosition){
        HashMap<String, LatLng> nearbySch = new HashMap<String, LatLng>();
        for (School sch : schoolList){
            LatLng latLng = sch.getLatLng();
            // Filtering schools based on distance
            if (distance(userPosition.latitude, userPosition.longitude, latLng.latitude, latLng.longitude) <= 5){
                nearbySch.put(sch.getSchoolName(), latLng);
            }
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

}
