package com.example.myapplication;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the MapController which controls the functions related to the map or distances
 */
public class MapController {
    /**
     * Get the latitude and longitude from a list of schools and output as a HashMap
     * @param context The current context of the application
     * @param schoolList The list of schools
     * @return HashMap mapping the latitude and longitude to the name of each school
     */
    public static HashMap<String, LatLng> getLatLong(Context context, List<School> schoolList){
        HashMap<String, LatLng> nearbySch = new HashMap<>();
        for (School sch : schoolList){
            LatLng latLng = getLocationFromAddress(context, sch.getAddress());
            nearbySch.put(sch.getSchoolName(), latLng);
        }
        return nearbySch;
    }

    /**
     * Calculates the distance between two locations given their latitudes and longitudes
     * @param lat1 The latitude of location 1
     * @param lng1 The longitude of location 1
     * @param lat2 The latitude of location 2
     * @param lng2 The longitude of location 2
     * @return The distance between the two locations
     */
    public static double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 6371; // in miles, change to 6371 for kilometer output

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadius * c; // output distance, in MILES
    }

    /**
     * Get the latitude and longitude from an address in string format
     * @param context The current context of the application
     * @param strAddress The address of the location in string
     * @return The latitude and longitude of the location stored in LatLng format
     */
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

    /**
     * Get the HashMap of schools and their locations if they are within 5km radius from the user's address
     * @param context The current contexxt of the application
     * @param latLongList The HashMap of schools and their respective locations in LatLng
     * @param userPosition The latitude and longitude of the user's address
     * @return The HashMap of schools and thier locations within a 5km radius
     */
    public static HashMap<String, LatLng> getLatLong(Context context, HashMap<String, LatLng> latLongList, LatLng userPosition) {
        HashMap<String, LatLng> nearbySch = new HashMap<>();
        for (Map.Entry<String, LatLng> entry : latLongList.entrySet()){
            // Filtering schools based on distance
            if (distance(userPosition.latitude, userPosition.longitude, entry.getValue().latitude, entry.getValue().longitude) <= 5){
                nearbySch.put(entry.getKey(), entry.getValue());
            }
        }
        System.out.println("getLatLong");
        return nearbySch;
    }

    /**
     * Checks if an address in string is valid
     * @param context The current context of the application
     * @param strAddress The address in string format
     * @return boolean indicating validity of the address
     */
    public static boolean isValidAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5, 1.225709, 103.602018, 1.473700, 104.025147);
            return address != null && address.size() >= 1;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
