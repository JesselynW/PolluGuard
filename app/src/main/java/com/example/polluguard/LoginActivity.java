package com.example.polluguard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button loginButton;
    private DBHelper db;
    CheckBox cb;
    TextView regis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
        boolean isLoggedIn = sp.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        etEmail = findViewById(R.id.emailEditText);
        etPassword = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        regis = findViewById(R.id.registerLink);
        cb = findViewById(R.id.checkbox);

        db = new DBHelper(this);

        loginButton.setOnClickListener(e -> {
            String email, pass;
            email = etEmail.getText().toString();
            pass = etPassword.getText().toString();

            if(email.equals("") || pass.equals("")){
                Toast.makeText(LoginActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }
            else{
                Cursor cursor = db.getUserbyEmailandPass(email, pass);

                if(cursor != null && cursor.moveToFirst()){

                    int userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));

                    Log.i("user data", "id: " + userId);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("user_id", userId);

                    if (cb.isChecked()) {
                        editor.putBoolean("isLoggedIn", true);
                    }

                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                    cursor.close();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }

            }

        });

        regis.setOnClickListener(e -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}