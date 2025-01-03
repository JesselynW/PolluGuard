package com.example.polluguard.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.polluguard.DBHelper;
import com.example.polluguard.R;

public class ProjectDetails extends AppCompatActivity {

    TextView dateTimeTV, organizerTV, descTV, aboutTV, locationTV, rewardTV, slotTV, nameTV;
    ImageView projectImage, logo;
    Button participateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_project_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();

        String dateTime = intent.getStringExtra("date") + ", " + intent.getStringExtra("time");
        int project = intent.getIntExtra("image", 0);
        String organizerName = intent.getStringExtra("organizerName");
        int organizerLogo = intent.getIntExtra("organizerLogo", 0);
        String organizerDesc = intent.getStringExtra("organizerDesc");
        String about = intent.getStringExtra("desc");
        String location = intent.getStringExtra("location");
        String reward = intent.getIntExtra("reward", 0) + " Volunteer Points";
        String slot = intent.getIntExtra("slot", 0) + " left";
        String name = intent.getStringExtra("projectName");

        dateTimeTV = findViewById(R.id.dateTimeText);
        organizerTV = findViewById(R.id.organizerName);
        descTV = findViewById(R.id.organizerDesc);
        aboutTV = findViewById(R.id.aboutText);
        locationTV = findViewById(R.id.locationText);
        rewardTV = findViewById(R.id.rewardText);
        slotTV = findViewById(R.id.slotText);
        projectImage = findViewById(R.id.projectImage);
        logo = findViewById(R.id.logoImage);
        nameTV = findViewById(R.id.judulText);

        dateTimeTV.setText(dateTime);
        projectImage.setImageResource(project);
        organizerTV.setText(organizerName);
        logo.setImageResource(organizerLogo);
        descTV.setText(organizerDesc);
        aboutTV.setText(about);
        locationTV.setText(location);
        rewardTV.setText(reward);
        slotTV.setText(slot);
        nameTV.setText(name);

        SharedPreferences sp = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = sp.getInt("user_id", 0);

        DBHelper db = new DBHelper(this);
        participateButton = findViewById(R.id.participateButton);

        participateButton.setOnClickListener(e -> {
            db.insertUserVolunteerEvent(userId, name);

            String link = intent.getStringExtra("linkWhatsapp");
            int qr = intent.getIntExtra("qr", 0);

            Intent it = new Intent(ProjectDetails.this, EventRegistrationActivity.class);
            it.putExtra("link", link);
            it.putExtra("qr", qr);

            startActivity(it);
        });

    }
}