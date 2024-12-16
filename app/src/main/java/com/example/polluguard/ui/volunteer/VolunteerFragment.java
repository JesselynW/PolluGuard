package com.example.polluguard.ui.volunteer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polluguard.DBHelper.ProjectDBHelper;
import com.example.polluguard.R;
import com.example.polluguard.adapter.*;
import com.example.polluguard.databinding.FragmentVolunteerBinding;
import com.example.polluguard.model.Project;
import com.example.polluguard.recyclerView.projectOnClick;
import com.example.polluguard.ui.ProjectDetails;

import java.util.ArrayList;

public class VolunteerFragment extends Fragment implements projectOnClick {

    private previewProjectAdapter previewAdapter;
    private projectAdapter projectAdapter;
    private ArrayList<Project> previews, projects;
    private FragmentVolunteerBinding binding;
    private RecyclerView previewRV, projectRV;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        VolunteerViewModel volunteerViewModel =
                new ViewModelProvider(this).get(VolunteerViewModel.class);

        binding = FragmentVolunteerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        previewRV = root.findViewById(R.id.previewRecyclerView);
        previewRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        projectRV = root.findViewById(R.id.projectRecyclerView);
        projectRV.setLayoutManager(new GridLayoutManager(getContext(), 2));

        ProjectDBHelper dbHelper = new ProjectDBHelper(getContext());
        previews = dbHelper.getThreeProjectInformation();
        projects = dbHelper.getAllProjectInformation();

        previewAdapter = new previewProjectAdapter(getContext(), previews);
        previewRV.setAdapter(previewAdapter);

        projectAdapter = new projectAdapter(getContext(), projects, this);
        projectRV.setAdapter(projectAdapter);

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
        startActivity(intent);
    }
}