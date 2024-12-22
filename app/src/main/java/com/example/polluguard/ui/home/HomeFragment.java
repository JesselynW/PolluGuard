package com.example.polluguard.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.example.polluguard.DBHelper;
import com.example.polluguard.MapFragment;
import com.example.polluguard.R;
import com.example.polluguard.databinding.FragmentHomeBinding;
import com.example.polluguard.model.User;
import com.example.polluguard.network.AirQuality;
import com.example.polluguard.network.AirQualityResponse;
import com.example.polluguard.network.AirQualityService;
import com.example.polluguard.network.RetrofitClient;
import com.example.polluguard.tools.AirPollutionLevel;
import com.example.polluguard.tools.LocationBoundCalculator;
import com.example.polluguard.tools.NearestDistanceCalculator;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;
    private ActivityResultLauncher<String[]> locationPermissionRequest;

    private FragmentHomeBinding binding;
    SharedPreferences sp;

    Button btnLearnMore, btnMap, btnVolunteer;
    TextView etName, etLocation, etAqi, etStatus;
    ImageView ivProfile;

    private DBHelper dbHelper;
    private HomeViewModel homeViewModel;

    private final double RADIUS = 10;
    private String locationBoundString;
    private AirQualityService service;
    private String waqiApiKey = "ce00656b30945718db12018a8ee5468c98a68cdf";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        sp = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
//        int id = sp.getInt("user_id", -1);

        etName = binding.name;
        ivProfile = binding.image;

        btnLearnMore = binding.learnMoreButton;
        btnMap = binding.mapButton;
        btnVolunteer = binding.volunteerButton;

        etStatus = binding.status;
        etLocation = binding.currentLocation;
        etAqi = binding.aqi;

        homeViewModel.initialize(getContext());

        homeViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            if(user != null) {
                etName.setText(user.getName());
                ivProfile.setImageBitmap(user.getImage());
            }
        });


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());


        locationPermissionRequest = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    Boolean fineLocation = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                    Boolean coarseLocation = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);

                    if (fineLocation || coarseLocation) {
                        getNearestLocationAqi();

//                        Log.i("NEAREST LOCATION DI LUAR", "LOCATION =  " + nearestStation + " aqi =  " + nearestLocationAqi);


                    } else {
                        Toast.makeText(requireContext(), "Permission not granted.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });



        btnLearnMore.setOnClickListener(v -> {
            View popupView = getLayoutInflater().inflate(R.layout.popup_information, null);

            int viewWidth = v.getWidth();

            PopupWindow popupWindow = new PopupWindow(popupView,
                    viewWidth,
                    LinearLayout.LayoutParams.WRAP_CONTENT);


            popupWindow.setOutsideTouchable(true);

            popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

            View container2 = popupWindow.getContentView().getRootView();
            Context context = popupWindow.getContentView().getContext();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

            WindowManager.LayoutParams params = (WindowManager.LayoutParams) container2.getLayoutParams();
            params.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.dimAmount = 0.5f; // Tingkat kegelapan (0.0 - 1.0)
            wm.updateViewLayout(container2, params);
        });

        btnMap.setOnClickListener(v -> {
            NavOptions options = new NavOptions.Builder()
                    .setPopUpTo(R.id.navigation_home, false) // False agar back stack tidak dihapus
                    .build();
            Navigation.findNavController(v).navigate(R.id.navigation_map, null, options);
        });

        btnVolunteer.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.home_to_volunteer);
        });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getNearestLocationAqi() {

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(requireContext(), "Permission not granted", Toast.LENGTH_SHORT).show();
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        Double userLat = location.getLatitude();
                        Double userLon = location.getLongitude();
                        Log.i("test di dalam", " UserLat = " + userLat + " UserLon = " + userLon);

                        getLocationBound(userLat, userLon);
                        fetchNearestLocationAQIData(userLat, userLon);

                    } else {
                        Toast.makeText(requireContext(), "Failed to get location.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getLocationBound(Double userLat, Double userLon){
        double[] locationBound = LocationBoundCalculator.calculateLocationBound(userLat, userLon, RADIUS);
        locationBoundString = locationBound[0] + "," + locationBound[1] + "," + locationBound[2] + "," + locationBound[3];
    }



    public void fetchNearestLocationAQIData(Double userLat, Double userLon){

        service = RetrofitClient.getAirQualityService();
        Call<AirQualityResponse> call = service.getAQIByBounds(locationBoundString, "all", waqiApiKey);

        call.enqueue(new Callback<AirQualityResponse>() {
            @Override
            public void onResponse(Call<AirQualityResponse> call, Response<AirQualityResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    double minDistance = RADIUS;
                    String nearestStation = "";
                    String nearestLocationAqi = "";

                    AirQualityResponse airQualityResponse = response.body();
                    List<AirQuality> airQualityList = airQualityResponse.getData();
                    Log.d("API Response", response.body().toString());
                    for(AirQuality airQuality:airQualityList){

                        double lat = airQuality.getLat();
                        double lon = airQuality.getLon();
                        double aqi = airQuality.getAqi();

                        double distance = NearestDistanceCalculator.haversineDistance(userLat, userLon, lat, lon);

                        if(distance < minDistance){
                            minDistance = distance;
                            nearestStation = airQuality.getStation().getName();
                            nearestLocationAqi = Double.toString(aqi);
                        }

                    }

                    Log.i("NEAREST LOCATION", "LOCATION =  " + nearestStation + " aqi =  " + nearestLocationAqi);
                    String nearestAqiLevel = AirPollutionLevel.getAQILevel(Double.parseDouble(nearestLocationAqi));
                    etStatus.setText(nearestAqiLevel);
                    etLocation.setText(nearestStation);
                    etAqi.setText(nearestLocationAqi);
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