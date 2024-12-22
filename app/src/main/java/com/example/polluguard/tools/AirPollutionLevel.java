package com.example.polluguard.tools;

public class AirPollutionLevel {

    public static String getAQILevel(double aqi){
        if (aqi <= 50){
            return "Good";
        }
        else if (aqi <= 100) {
            return "Moderate";
        }
        else if (aqi <= 150) {
            return "Unhealthy for Sensitive Groups";
        }
        else if (aqi <= 200){
            return "Unhealthy";
        }
        else if (aqi <= 300){
            return "Very Unhealthy";
        }
        else {
            return "Hazardous";
        }
    }
}
