package com.example.polluguard.network;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AirQualityService {
    @GET("v2/map/bounds")
    Call<AirQualityResponse> getAQIByBounds(
        @Query("latlng") String latlng,
        @Query("networks") String networks,
        @Query("token") String token
    );
}
