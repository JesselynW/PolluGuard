package com.example.polluguard.ui;

import android.content.Intent;
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

public class EventRegistrationActivity extends AppCompatActivity {

    ImageView qrWA;
    TextView linkWA;
    Button historyButton;

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

        //null
        qrWA = findViewById(R.id.qrImage);
        linkWA = findViewById(R.id.linkWA);

        qrWA.setImageResource(intent.getIntExtra("qr", 0));
        linkWA.setText(intent.getStringExtra("link"));

//        DBHelper db = new DBHelper(this);

        // edit disini el
//        historyButton.setOnClickListener(e -> {
//            Intent it = new Intent(EventRegistrationActivity.this, );
//            startActivity(it);
//        });

    }
}