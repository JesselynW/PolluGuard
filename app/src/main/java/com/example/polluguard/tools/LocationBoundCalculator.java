package com.example.polluguard.tools;

public class LocationBoundCalculator {
    public static double[] calculateLocationBound(double lat, double lon, double radius) {
        double[] result;

       double deltaLat = radius / 110.574;
       // approximately 1 deg lat = 110.574
        double deltaLon = radius / (111.32 * Math.cos(Math.toRadians(lat)));
        // approximately 1 deg lng = 111.32 * cos(latitude)

        double minLat = lat - deltaLat;
        double maxLat = lat + deltaLat;
        double minLon = lon - deltaLon;
        double maxLon = lon + deltaLon;

        result = new double[]{minLat, minLon, maxLat, maxLon};

        return result;
    }
}
