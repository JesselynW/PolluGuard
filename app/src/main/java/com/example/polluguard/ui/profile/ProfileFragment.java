package com.example.polluguard.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.polluguard.databinding.FragmentProfileBinding;
import com.example.polluguard.ui.volunteer.VolunteerViewModel;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    TextView etName, etPoint;
    ImageView ivProfile;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        etName = binding.name;
        etPoint = binding.point;
        ivProfile = binding.image;

       profileViewModel.initialize(getContext());

       profileViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
           if(user != null){
                etName.setText(user.getName());
                etPoint.setText(user.getVolunteerPoints());
                ivProfile.setImageBitmap(user.getImage());
           }
       });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}