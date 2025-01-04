package com.example.polluguard.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.polluguard.R;

public class ArticleDetails extends AppCompatActivity {

    TextView title, author, content;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_article_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();

        title = findViewById(R.id.headlineText);
        title.setText(intent.getStringExtra("title"));
        author = findViewById(R.id.dateText);
        author.setText(intent.getStringExtra("author") + " - " + intent.getStringExtra("date"));
        content = findViewById(R.id.contentText);
        content.setText(intent.getStringExtra("content"));
        image = findViewById(R.id.articleImage);
        image.setImageResource(intent.getIntExtra("image", 0));


    }
}