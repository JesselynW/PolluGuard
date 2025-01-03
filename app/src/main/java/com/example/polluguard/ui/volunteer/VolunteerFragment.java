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

import com.example.polluguard.DBHelper;
import com.example.polluguard.R;
import com.example.polluguard.adapter.*;
import com.example.polluguard.databinding.FragmentVolunteerBinding;
import com.example.polluguard.model.Project;
import com.example.polluguard.recyclerView.ProjectOnClick;
import com.example.polluguard.ui.ProjectDetails;

import java.util.ArrayList;

public class VolunteerFragment extends Fragment implements ProjectOnClick {

    private PreviewProjectAdapter previewAdapter;
    private ProjectAdapter projectAdapter;
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

        DBHelper dbHelper = new DBHelper(getContext());
        previews = dbHelper.getThreeProjectInformation();
        projects = dbHelper.getAllProjectInformation();

        previewAdapter = new PreviewProjectAdapter(getContext(), previews);
        previewRV.setAdapter(previewAdapter);

        projectAdapter = new ProjectAdapter(getContext(), projects, this);
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
        intent.putExtra("linkWhatsapp", projects.get(position).getLinkWA());
        intent.putExtra("qr", projects.get(position).getQr());

        startActivity(intent);
    }
}