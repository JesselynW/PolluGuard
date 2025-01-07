package com.example.polluguard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import java.util.ArrayList;

public class UserProjectActivity extends AppCompatActivity {

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

        userProjectAdapter = new UserProjectAdapter(this, id, userProjects);
        userProjectsRV.setAdapter(userProjectAdapter);

    }

}