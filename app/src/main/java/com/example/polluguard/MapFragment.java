package com.example.polluguard;//package com.example.polluguard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.polluguard.network.AirQuality;
import com.example.polluguard.network.AirQualityResponse;
import com.example.polluguard.network.AirQualityService;
import com.example.polluguard.network.RetrofitClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment {
//    private GoogleMap googleMap;
    private AirQualityService service;
    private String waqiApiKey = "ce00656b30945718db12018a8ee5468c98a68cdf";
    private final String BOUND_INDONESIA = "-11.0,95.0,6.0,141.0";

    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            fetchAQIDataForIndonesia(googleMap);

            LatLng indonesiaCenter = new LatLng(-2.5, 118.0); // Center of Indonesia
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(indonesiaCenter, 5));

            // Fetch AQI data for Indonesia
            fetchAQIDataForIndonesia(googleMap);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    public void fetchAQIDataForIndonesia(GoogleMap googleMap){
        service = RetrofitClient.getAirQualityService();
        Call<AirQualityResponse> call = service.getAQIByBounds(BOUND_INDONESIA, "all", waqiApiKey);

        call.enqueue(new Callback<AirQualityResponse>() {
            @Override
            public void onResponse(Call<AirQualityResponse> call, Response<AirQualityResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    AirQualityResponse airQualityResponse = response.body();
                    List<AirQuality> airQualityList = airQualityResponse.getData();
                    Log.d("API Response", response.body().toString());
                    for(AirQuality airQuality:airQualityList){

                        double lat = airQuality.getLat();
                        double lon = airQuality.getLon();
                        double aqi = airQuality.getAqi();

                        String station = airQuality.getStation().getName();
//                        Log.i("test api", "lat " + lat + "lon " + lon + "aqi " + aqi);
//                        Log.i("test api station", "station"  + airQuality.getStation().getName());
                        String aqiSnippet = "AQI: " + aqi;
//                        if(airQuality.getStation() != null){
//                            Log.e("test", "test bang");
//                            String name = airQuality.getStation().getName();
//                            String aqiSnippet = "AQI: " + aqi;
//                            LatLng location = new LatLng(lat, lon);
//                            googleMap.addMarker(new MarkerOptions()
//                                    .position(location)
//                                    .title(name)
//                                    .snippet(aqiSnippet));
//                        }

                        LatLng location = new LatLng(lat, lon);
                        googleMap.addMarker(new MarkerOptions()
                                .position(location)
                                .title(station)
                                .snippet(aqiSnippet));

                    }
                }
                else {
                    Toast.makeText(getContext(), "Failed to get API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AirQualityResponse> call, Throwable throwable) {
                Log.e("MapFragment", "API call failed: " + throwable.getMessage());
                Toast.makeText(getContext(), "Failed to get API kah?", Toast.LENGTH_SHORT).show();
            }
        });
    }

}