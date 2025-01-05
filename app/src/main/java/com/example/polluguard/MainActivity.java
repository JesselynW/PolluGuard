package com.example.polluguard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.polluguard.databinding.ActivityHomeBinding;

public class MainActivity extends AppCompatActivity {


    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        BottomNavigationView navView = findViewById(R.id.nav_view);
//
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_volunteer, R.id.navigation_map, R.id.navigation_discover, R.id.navigation_profile)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
//
//        NavigationUI.setupWithNavController(binding.navView, navController);

        Intent intent = getIntent();

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);

        // Find the NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_home);
        NavController navController = navHostFragment.getNavController();

        // Link BottomNavigationView with NavController
//        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        if(intent.getBooleanExtra("toProfile", false))
            navController.navigate(R.id.navigation_profile);

        Log.i("event registration activity", "" + intent.getBooleanExtra("toProfile", false));

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            Log.d("Navigation", "Navigated to: " + destination.getId());
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {

            if(item.getItemId() == R.id.navigation_home){
                navController.navigate(R.id.navigation_home);
            }
            else if(item.getItemId() == R.id.navigation_volunteer){
                navController.navigate(R.id.navigation_volunteer);
            }
            else if(item.getItemId() == R.id.navigation_map){
                navController.navigate(R.id.navigation_map);
            }
            else if(item.getItemId() == R.id.navigation_discover){
                navController.navigate(R.id.navigation_discover);
            }
            else if(item.getItemId() == R.id.navigation_profile){
                navController.navigate(R.id.navigation_profile);
            }
            return true;
        });

    }

}