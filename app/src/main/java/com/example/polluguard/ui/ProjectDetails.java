package com.example.polluguard.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.polluguard.DBHelper;
import com.example.polluguard.R;
import com.example.polluguard.model.Project;

public class ProjectDetails extends AppCompatActivity {

    TextView dateTimeTV, organizerTV, descTV, aboutTV, locationTV, rewardTV, slotTV, nameTV, text, percentage;
    ImageView projectImage, logo;
    Button participateButton;
    ImageView backButton;
    ProgressBar progressBar;

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
        DBHelper db = new DBHelper(this);

        String dateTime = intent.getStringExtra("date") + ", " + intent.getStringExtra("time");
        int project = intent.getIntExtra("image", 0);
        String organizerName = intent.getStringExtra("organizerName");
        int organizerLogo = intent.getIntExtra("organizerLogo", 0);
        String organizerDesc = intent.getStringExtra("organizerDesc");
        String about = intent.getStringExtra("desc");
        String location = intent.getStringExtra("location");
        String reward = intent.getIntExtra("reward", 0) + " Volunteer Points";
        String slot = intent.getIntExtra("slotLeft", 0) + " left";
        String name = intent.getStringExtra("projectName");
        String link = intent.getStringExtra("linkWhatsapp");
        int qr = intent.getIntExtra("qr", 0);

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
        progressBar = findViewById(R.id.progressBar);
        percentage = findViewById(R.id.slotPercentageText);
        backButton = findViewById(R.id.backButton);

        double slotPercentage = 1 - ((double)intent.getIntExtra("slotLeft", 0)/(double)intent.getIntExtra("slot", 0));
        slotPercentage *= 100;

        Log.i("persen slotnya", " " + slotPercentage + intent.getIntExtra("slotLeft", 0) + intent.getIntExtra("slot", 0));

        dateTimeTV.setText(dateTime);
        projectImage.setImageResource(project);
        organizerTV.setText(organizerName);
        logo.setImageResource(organizerLogo);
        descTV.setText(organizerDesc);
        aboutTV.setText(about);
        locationTV.setText(location);
        rewardTV.setText(reward);
        progressBar.setProgress((int) slotPercentage);
        percentage.setText((int) slotPercentage + "%");
        slotTV.setText(slot);
        nameTV.setText(name);

        SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
        int userId = sp.getInt("user_id", -1);

        participateButton = findViewById(R.id.participateButton);
        text = findViewById(R.id.alreadyRegisteredText);
        Log.d("ProjectDetails", "participateButton is " + (participateButton == null ? "null" : "not null"));


        backButton.setOnClickListener(v -> {
            onBackPressed();
        });

        if(db.checkUserVolunteered(userId, name)){
            participateButton.setVisibility(View.VISIBLE);
            participateButton.setOnClickListener(e -> {
                db.insertUserVolunteerEvent(userId, name);

                Intent it = new Intent(ProjectDetails.this, EventRegistrationActivity.class);
                it.putExtra("link", link);
                it.putExtra("qrWA", qr);

                startActivity(it);
            });

            text.setVisibility(View.GONE);
        } else if (slotPercentage == 100) {
            participateButton.setVisibility(View.GONE);
            text.setVisibility(View.GONE);
            text.setText("Already full");
        } else{
            participateButton.setVisibility(View.GONE);
            text.setVisibility(View.VISIBLE);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        DBHelper db = new DBHelper(this);
        SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
        int userId = sp.getInt("user_id", -1);
        Intent intent = getIntent();

        String name = intent.getStringExtra("projectName");
        String link = intent.getStringExtra("linkWhatsapp");
        int qr = intent.getIntExtra("qr", 0);
        String slot = intent.getIntExtra("slotLeft", 0) + " left";

        double slotPercentage = 1 - ((double)intent.getIntExtra("slotLeft", 0)/(double)intent.getIntExtra("slot", 0));
        slotPercentage *= 100;

        progressBar.setProgress((int) slotPercentage);
        percentage.setText((int) slotPercentage + "%");
        slotTV.setText(slot);

        if(db.checkUserVolunteered(userId, name)){
            participateButton.setVisibility(View.VISIBLE);
            participateButton.setOnClickListener(e -> {
                db.insertUserVolunteerEvent(userId, getIntent().getStringExtra("projectName"));

                Intent it = new Intent(ProjectDetails.this, EventRegistrationActivity.class);
                it.putExtra("link", link);
                it.putExtra("qrWA", qr);

                startActivity(it);
            });

            text.setVisibility(View.GONE);
        } else if (slotPercentage == 100) {
            participateButton.setVisibility(View.GONE);
            text.setVisibility(View.GONE);
            text.setText("Already full");
        } else{
            participateButton.setVisibility(View.GONE);
            text.setVisibility(View.VISIBLE);
        }
    }
}