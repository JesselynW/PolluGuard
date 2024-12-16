package com.example.polluguard.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.polluguard.DBHelper;
import com.example.polluguard.databinding.FragmentHomeBinding;
import com.example.polluguard.model.User;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    SharedPreferences sp;

    TextView etName;
    ImageView ivProfile;

    private DBHelper dbHelper;
    private HomeViewModel homeViewModel;

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

        homeViewModel.initialize(getContext());


        homeViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            if(user != null) {
                etName.setText(user.getName());
                ivProfile.setImageBitmap(user.getImage());
            }
        });

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}