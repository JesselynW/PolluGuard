package com.example.polluguard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.polluguard.adapter.UserProjectAdapter;
import com.example.polluguard.model.Project;
import com.example.polluguard.recyclerView.ProjectOnClick;
import com.example.polluguard.ui.ProjectDetails;

import java.util.ArrayList;

public class UserProjectActivity extends AppCompatActivity implements ProjectOnClick {

    private ArrayList<Project> userProjects;
    private UserProjectAdapter userProjectAdapter;
    private RecyclerView userProjectsRV;

    private ImageView backButton;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_project);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> {
            onBackPressed();
        });

        userProjectsRV = findViewById(R.id.userProjectRecyclerView_userProjectActivity);
        userProjectsRV.setLayoutManager(new GridLayoutManager(this, 2));

        sp = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        int id = sp.getInt("user_id", -1);

        DBHelper dbHelper = new DBHelper(this);
        userProjects = dbHelper.getAllUserProject(id);

        userProjectAdapter = new UserProjectAdapter(this, id, userProjects, this);
        userProjectsRV.setAdapter(userProjectAdapter);

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, ProjectDetails.class);
        DBHelper dbHelper = new DBHelper(this);

//        intent.putExtra("project", projects.get(position));
        Log.i("project", "Project: " +  userProjects.get(position));
        intent.putExtra("projectName", userProjects.get(position).getProjectName());
        intent.putExtra("date", userProjects.get(position).getDate());
        intent.putExtra("time", userProjects.get(position).getTime());
        intent.putExtra("image", userProjects.get(position).getImageProject());
        intent.putExtra("organizerName", userProjects.get(position).getOrganizer().getOrganizerName());
        intent.putExtra("organizerLogo", userProjects.get(position).getOrganizer().getOrganizerLogo());
        intent.putExtra("organizerDesc", userProjects.get(position).getOrganizer().getOrganizerDesc());
        intent.putExtra("desc", userProjects.get(position).getAbout());
        intent.putExtra("location", userProjects.get(position).getLocation());
        intent.putExtra("reward", userProjects.get(position).getReward());
        intent.putExtra("slot", userProjects.get(position).getSlot());
        intent.putExtra("slotLeft", dbHelper.countSlotbyEventId(userProjects.get(position)));
        intent.putExtra("linkWhatsapp", userProjects.get(position).getLinkWA());
        intent.putExtra("qr", userProjects.get(position).getQr());

        startActivity(intent);
    }
}