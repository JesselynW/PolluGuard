package com.example.polluguard.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polluguard.DBHelper;
import com.example.polluguard.R;
import com.example.polluguard.adapter.CommentAdapter;
import com.example.polluguard.model.Comment;

import java.util.ArrayList;

public class ArticleDetails extends AppCompatActivity {

    TextView title, author, content;
    ImageView image;
    CommentAdapter commentAdapter;
    RecyclerView commentRV;
    ArrayList<Comment> comments;
    Button post;
    EditText commentET;

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

        DBHelper dbHelper = new DBHelper(this);

        SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
        Log.i("user id", "user id: " + sp.getInt("user_id", 0));

        commentET = findViewById(R.id.commentEditText);
        post = findViewById(R.id.postButton);
        int articleId = dbHelper.getArticleId(intent.getStringExtra("title"), intent.getStringExtra("date"));

        post.setOnClickListener(e -> {
            dbHelper.insertArticleComment(commentET.getText().toString(), sp.getInt("user_id", 0), articleId);

            Log.i("articleId", commentET.getText().toString() + sp.getInt("user_id", 0) + " id article " + articleId);
           comments = dbHelper.getAllArticleComment(articleId);
           commentAdapter.setData(comments);
           commentAdapter.notifyDataSetChanged();

           commentET.setText("");
        });



        commentRV = findViewById(R.id.commentRV);
        commentRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        comments = dbHelper.getAllArticleComment(articleId);
//        Log.i("nama user di posisi pertama", "nama user: " + comments.get(0).getUsername());

        commentAdapter = new CommentAdapter(this, comments);
        commentRV.setAdapter(commentAdapter);

    }
}