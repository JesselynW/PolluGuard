package com.example.polluguard.ui.map;//package com.example.polluguard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polluguard.R;
import com.example.polluguard.network.AirQuality;
import com.example.polluguard.network.AirQualityResponse;
import com.example.polluguard.network.AirQualityService;
import com.example.polluguard.network.RetrofitClient;
import com.example.polluguard.tools.AirPollutionLevel;
import com.example.polluguard.ui.home.ProgressBarCircle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment {

    private AirQualityService service;
    private String waqiApiKey = "ce00656b30945718db12018a8ee5468c98a68cdf";
    private final String BOUND_INDONESIA = "-11.0,95.0,6.0,141.0";

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            fetchAQIDataForIndonesia(googleMap);

            LatLng indonesiaCenter = new LatLng(-2.5, 118.0); // Center of Indonesia
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(indonesiaCenter, 6));

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

                        Bitmap customMarker = customMarker(requireContext(), aqi);
                        LatLng location = new LatLng(lat, lon);
                        Marker marker = googleMap.addMarker(new MarkerOptions()
                                .position(location)
                                .icon(BitmapDescriptorFactory.fromBitmap(customMarker)));
                        marker.setTag(airQuality);
                    }

                    googleMap.setOnMarkerClickListener(m -> {
                        AirQuality airQuality = (AirQuality) m.getTag();

                        if(airQuality != null){
                            double aqi = airQuality.getAqi();
                            String location = airQuality.getStation().getName();
                            String aqiLevel = AirPollutionLevel.getAQILevel(aqi);

                            showPopUp(aqi, location, aqiLevel);
                        }
                        return true;
                    });

                    googleMap.setOnMapClickListener(latLng -> hidePopUp());
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

    public void showPopUp(double aqi, String location, String aqiLevel) {
        View view = getView().findViewById(R.id.popup_map_info);
        view.setVisibility(View.VISIBLE);

        TextView etStatus = view.findViewById(R.id.status);
        TextView etLocation = view.findViewById(R.id.location);
        TextView etAqi = view.findViewById(R.id.aqi);
        TextView etLastUpdated = view.findViewById(R.id.last_updated);
        LinearLayout statusContainer = view.findViewById(R.id.statusContainer);
        ProgressBarCircle aqiCircle = view.findViewById(R.id.progressBar);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, dd MMMM yyyy");
        String currDateTime = sdf.format(new Date());

        etLastUpdated.setText("Last Updated " + currDateTime);
        aqiCircle.setAqiPercentage((float) aqi);
        etStatus.setText(aqiLevel);
        etLocation.setText(location);
        etAqi.setText(String.valueOf((int)aqi));
        etStatus.setTextColor(ContextCompat.getColor(getContext(),R.color.white));

        switch (aqiLevel) {
            case "Good":
                aqiCircle.setCircleColor(R.color.aqiGood);
                statusContainer.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.aqiGood)));
                break;
            case "Moderate":
                aqiCircle.setCircleColor(R.color.aqiModerate);
                statusContainer.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.aqiModerate)));
                etStatus.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
                break;
            case "Unhealthy for Sensitive Groups":
                aqiCircle.setCircleColor(R.color.aqiUnhealthySensitiveGroup);
                statusContainer.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.aqiUnhealthySensitiveGroup)));
                break;
            case "Unhealthy":
                aqiCircle.setCircleColor(R.color.aqiUnhealthy);
                statusContainer.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.aqiUnhealthy)));
                break;
            case "Very Unhealthy":
                aqiCircle.setCircleColor(R.color.aqiVeryUnhealthy);
                statusContainer.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.aqiVeryUnhealthy)));
                break;
            case "Hazardous":
                aqiCircle.setCircleColor(R.color.aqiHazardous);
                statusContainer.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.aqiHazardous)));
                break;
            default:
                aqiCircle.setCircleColor(R.color.darkGray);
                statusContainer.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.darkGray)));
                break;
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(aqiCircle, "aqiPercentage", 0, (int)aqi);
        animator.setDuration(2000);
        animator.start();
    }

    public void hidePopUp() {
        View view = getView().findViewById(R.id.popup_map_info);
        view.setVisibility(View.GONE);
    }

    public Bitmap customMarker(Context context, double aqi) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View marker = inflater.inflate(R.layout.custom_marker, null);

        ImageView iconMarker = marker.findViewById(R.id.marker);
        TextView textMarker = marker.findViewById(R.id.aqiValue);

        String aqiLevel = AirPollutionLevel.getAQILevel(aqi);

        switch (aqiLevel) {
            case "Good":
                iconMarker.setImageResource(R.drawable.location_good);
                break;
            case "Moderate":
                iconMarker.setImageResource(R.drawable.location_moderate);
                break;
            case "Unhealthy for Sensitive Groups":
                iconMarker.setImageResource(R.drawable.location_unhealthy_sensitive);
                break;
            case "Unhealthy":
                iconMarker.setImageResource(R.drawable.location_unhealthy);
                break;
            case "Very Unhealthy":
                iconMarker.setImageResource(R.drawable.location_very_unhealthy);
                break;
            case "Hazardous":
                iconMarker.setImageResource(R.drawable.location_hazardous);
                break;
            default:
                iconMarker.setImageResource(R.drawable.location_good);
                break;
        }

        textMarker.setText(String.valueOf((int)aqi));

        marker.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        marker.layout(0, 0, marker.getMeasuredWidth(), marker.getMeasuredHeight());
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }
}