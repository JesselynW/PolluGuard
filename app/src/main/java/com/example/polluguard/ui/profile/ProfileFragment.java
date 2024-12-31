package com.example.polluguard.ui.profile;

import static android.content.Context.MODE_PRIVATE;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.polluguard.EditProfileActivity;
import com.example.polluguard.LoginActivity;
import com.example.polluguard.UserProjectActivity;
import com.example.polluguard.adapter.UserProjectAdapter;
import com.example.polluguard.databinding.FragmentProfileBinding;
import com.example.polluguard.model.Project;
import com.example.polluguard.recyclerView.ProjectOnClick;
import com.example.polluguard.ui.volunteer.VolunteerViewModel;

import java.util.ArrayList;

public class ProfileFragment extends Fragment implements ProjectOnClick {

    private FragmentProfileBinding binding;

    private UserProjectAdapter userProjectAdapter;
    private ArrayList<Project> userProjects;
    private RecyclerView userProjectRV;

    private int userId;

    private SharedPreferences sp;

    private ProfileViewModel profileViewModel2;

    SwipeRefreshLayout swipeRefreshLayout;
    TextView etName, etPoint, etProjectSeeMore;
    ImageView ivProfile;
    Button logoutButton;
    LinearLayout editProfileButton;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sp = getContext().getSharedPreferences("UserData", MODE_PRIVATE);
        userId = sp.getInt("user_id", -1);

        swipeRefreshLayout = binding.refreshLayout;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshUserPoint(profileViewModel);
            }
        });

        userProjectRV = binding.userProjectRecyclerView;
        userProjectRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        etName = binding.name;
        etPoint = binding.point;
        ivProfile = binding.image;

        etProjectSeeMore = binding.projectSeeMore;

        logoutButton = binding.logoutButton;
        editProfileButton = binding.editProfileButton;

       profileViewModel.initialize(getContext());

       profileViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
           if(user != null){
                etName.setText(user.getName());
                etPoint.setText(Integer.toString(user.getVolunteerPoints()));
                ivProfile.setImageBitmap(user.getImage());
           }
       });

        userProjectAdapter = new UserProjectAdapter(getContext(), userId, new ArrayList<>(), this);
        userProjectRV.setAdapter(userProjectAdapter);

       profileViewModel.getUserProjectLiveData().observe(getViewLifecycleOwner(), userProjectsData -> {
           userProjectAdapter.updateData(userProjectsData);
           Log.i("USER PROJECT DATA", "TESTT= " + userProjectsData);
       });

       etProjectSeeMore.setOnClickListener(v -> {
           Intent intent = new Intent(getActivity(), UserProjectActivity.class);
           startActivity(intent);
       });

       editProfileButton.setOnClickListener(v -> {
           Intent intent = new Intent(getActivity(), EditProfileActivity.class);
           startActivity(intent);
       });

       logoutButton.setOnClickListener(v -> {
           SharedPreferences shp = v.getContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
           SharedPreferences.Editor editor = shp.edit();
           editor.putBoolean("isLoggedIn", false);
           editor.apply();

           Intent intent = new Intent(getActivity(), LoginActivity.class);
           startActivity(intent);
       });

        return root;
    }

    private void refreshUserPoint( ProfileViewModel profileViewModel) {
        swipeRefreshLayout.setRefreshing(true);
//        etName.setText("HAAAAAIII djafjadfas");
        profileViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                // When LiveData is updated, update UI accordingly
                etPoint.setText(Integer.toString(user.getVolunteerPoints()));

                // After data is refreshed, stop the refreshing animation
                swipeRefreshLayout.setRefreshing(false);
            }
        });
//        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(int position) {

    }
}