package com.example.myapplication;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Represents the Utility class for the formatting and style of the application
 */
public class Utils {
    /**
     * Creates a list of vibrant light colours
     */
    public static ColorDrawable[] vibrantLightColorList =
                {
                        new ColorDrawable(Color.parseColor("#ffeead")),
                        new ColorDrawable(Color.parseColor("#93cfb3")),
                        new ColorDrawable(Color.parseColor("#fd7a7a")),
                        new ColorDrawable(Color.parseColor("#faca5f")),
                        new ColorDrawable(Color.parseColor("#1ba798")),
                        new ColorDrawable(Color.parseColor("#6aa9ae")),
                        new ColorDrawable(Color.parseColor("#ffbf27")),
                        new ColorDrawable(Color.parseColor("#d93947"))
                };

    /**
     * Get a random colour from the list of vibrant light colours
     * @return A random colour
     */
    public static ColorDrawable getRandomDrawbleColor() {
            int idx = new Random().nextInt(vibrantLightColorList.length);
            return vibrantLightColorList[idx];
        }

    /**
     * Converts a date to time format
     * @param oldstringDate
     * @return
     */
    public static String DateToTimeFormat(String oldstringDate){
            PrettyTime p = new PrettyTime(new Locale(getCountry()));
            String isTime = null;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
                        Locale.ENGLISH);
                Date date = sdf.parse(oldstringDate);
                isTime = p.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return isTime;
        }

    /**
     * Reformats a date
     * @param oldstringDate The current date format
     * @return The new date format
     */
    public static String DateFormat(String oldstringDate){
            String newDate;
            SimpleDateFormat dateFormat = new SimpleDateFormat("E, d MMM yyyy", new Locale(getCountry()));
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(oldstringDate);
                newDate = dateFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                newDate = oldstringDate;
            }

            return newDate;
        }

    /**
     * Get the country the user belongs to
     * @return The country the user belongs to
     */
    public static String getCountry(){
            Locale locale = Locale.getDefault();
            String country = String.valueOf(locale.getCountry());
            return country.toLowerCase();
        }
    }

