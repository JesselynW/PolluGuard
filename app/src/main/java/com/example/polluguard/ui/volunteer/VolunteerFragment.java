package com.example.polluguard.ui.volunteer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polluguard.DBHelper;
import com.example.polluguard.R;
import com.example.polluguard.adapter.*;
import com.example.polluguard.databinding.FragmentVolunteerBinding;
import com.example.polluguard.model.Project;
import com.example.polluguard.recyclerView.ProjectOnClick;
import com.example.polluguard.ui.ProjectDetails;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class VolunteerFragment extends Fragment implements ProjectOnClick {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private PreviewProjectAdapter previewAdapter;
    private ProjectAdapter projectAdapter;
    private ArrayList<Project> previews, projects;
    private FragmentVolunteerBinding binding;
    private RecyclerView previewRV, projectRV;
    private double userLatitude, userLongitude;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        VolunteerViewModel volunteerViewModel =
                new ViewModelProvider(this).get(VolunteerViewModel.class);

        binding = FragmentVolunteerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }


        previewRV = root.findViewById(R.id.previewRecyclerView);
        previewRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        projectRV = root.findViewById(R.id.projectRecyclerView);
        projectRV.setLayoutManager(new GridLayoutManager(getContext(), 2));

        DBHelper dbHelper = new DBHelper(getContext());
        previews = dbHelper.getThreeProjectInformation();
        projects = dbHelper.getAllProjectInformation();

        previewAdapter = new PreviewProjectAdapter(getContext(), previews);
        previewRV.setAdapter(previewAdapter);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        userLatitude = location.getLatitude();
                        userLongitude = location.getLongitude();
                        Log.i("current location", "Latitude: " + userLatitude + " - " + userLongitude);

                        projectAdapter = new ProjectAdapter(getContext(), projects, this, userLatitude, userLongitude);
                        projectRV.setAdapter(projectAdapter);
                    }
                    else{
                        projectAdapter = new ProjectAdapter(getContext(), projects, this, userLatitude, userLongitude);
                        projectRV.setAdapter(projectAdapter);
                    }
                });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), ProjectDetails.class);
        DBHelper dbHelper = new DBHelper(getContext());

//        intent.putExtra("project", projects.get(position));
        Log.i("project", "Project: " +  projects.get(position));
        intent.putExtra("projectName", projects.get(position).getProjectName());
        intent.putExtra("date", projects.get(position).getDate());
        intent.putExtra("time", projects.get(position).getTime());
        intent.putExtra("image", projects.get(position).getImageProject());
        intent.putExtra("organizerName", projects.get(position).getOrganizer().getOrganizerName());
        intent.putExtra("organizerLogo", projects.get(position).getOrganizer().getOrganizerLogo());
        intent.putExtra("organizerDesc", projects.get(position).getOrganizer().getOrganizerDesc());
        intent.putExtra("desc", projects.get(position).getAbout());
        intent.putExtra("location", projects.get(position).getLocation());
        intent.putExtra("reward", projects.get(position).getReward());
        intent.putExtra("slot", projects.get(position).getSlot());
        intent.putExtra("slotLeft", dbHelper.countSlotbyEventId(projects.get(position)));
        intent.putExtra("linkWhatsapp", projects.get(position).getLinkWA());
        intent.putExtra("qr", projects.get(position).getQr());

        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            // Cek apakah izin diberikan
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin diberikan, ambil lokasi
                FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(location -> {
                            if (location != null) {
                                userLatitude = location.getLatitude();
                                userLongitude = location.getLongitude();
                            }
                        });
            } else {
                // Izin ditolak, beri tahu pengguna
                Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}