package com.example.polluguard.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.polluguard.DBHelper;
import com.example.polluguard.MainActivity;
import com.example.polluguard.R;
import com.example.polluguard.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EventRegistrationActivity extends AppCompatActivity {

    ImageView qrWA;
    TextView linkWA;
    Button historyButton;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();

        qrWA = findViewById(R.id.qrImage);
        linkWA = findViewById(R.id.linkWA);
        backButton = findViewById(R.id.backButton);

        qrWA.setImageResource(intent.getIntExtra("qrWA", 0));
        linkWA.setText(intent.getStringExtra("link"));

        backButton.setOnClickListener(v -> {
            onBackPressed();
        });

        historyButton = findViewById(R.id.historyButton);

        historyButton.setOnClickListener(e -> {
            Intent it = new Intent(EventRegistrationActivity.this, MainActivity.class);

            it.putExtra("toProfile", true);

            startActivity(it);


//            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.main, new ProfileFragment()).commit();
//
//            LinearLayout layout = findViewById(R.id.layout);
//            TextView gone, gone1, gone2;
//            gone = findViewById(R.id.gone);
//            gone1 = findViewById(R.id.gone1);
//            gone2 = findViewById(R.id.gone2);
//            layout.setVisibility(View.GONE);
//            gone.setVisibility(View.GONE);
//            gone1.setVisibility(View.GONE);
//            gone2.setVisibility(View.GONE);
//            qrWA.setVisibility(View.GONE);
//            linkWA.setVisibility(View.GONE);
//            historyButton.setVisibility(View.GONE);
        });

    }
}