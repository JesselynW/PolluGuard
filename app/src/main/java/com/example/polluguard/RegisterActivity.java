package com.example.polluguard;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.polluguard.model.User;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etEmail, etPassword, etConfPassword;
    ImageView ivProfile;
    Button registerButton;
    private Uri uri;
    private Bitmap bitmapImage;
    private DBHelper db;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            int currentTopPadding = v.getPaddingTop();
            v.setPadding(systemBars.left, currentTopPadding, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();

        etName = findViewById(R.id.nameEditText);
        etEmail = findViewById(R.id.emailEditText);
        etPassword = findViewById(R.id.passwordEditText);
        etConfPassword = findViewById(R.id.confirmPasswordEditText);
        ivProfile = findViewById(R.id.profilePicture);
        registerButton = findViewById(R.id.registerButton);
        db = new DBHelper(this);

        sp = getSharedPreferences("UserData", MODE_PRIVATE);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if(o.getResultCode() == Activity.RESULT_OK){
                    Intent data = o.getData();
                    assert data != null;
                    uri = data.getData();
                    try {
                        bitmapImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                    } catch(Exception e){
                        Log.e("ERRRROR", "Exception in onActivityResult : " + e.getMessage());
                    }
                    ivProfile.setImageBitmap(bitmapImage);
                }
                else {
                    Log.e("ERRRROR", "No Image Selected");
                }
            }
        });
        
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    activityResultLauncher.launch(intent);
                } catch (Exception e){
                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, email, password, confirmPassword;
                name = etName.getText().toString();
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                confirmPassword = etConfPassword.getText().toString();


                if(name.equals("") || email.equals("") || password.equals("") || confirmPassword.equals("")){
                    Toast.makeText(RegisterActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
                else if(ivProfile.getDrawable() == null || bitmapImage == null) {
                    Toast.makeText(RegisterActivity.this, "Please upload a profile picture", Toast.LENGTH_SHORT).show();
                }
                else if(!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Password do not match", Toast.LENGTH_SHORT).show();
                }
                else {
                    User user = new User(name, email, password, bitmapImage);
                    db.insertUser(user);
                    Cursor cursor = db.getUser();
                    int id = 0;
                    while(cursor.moveToNext()){
                        Log.i("User Check", "User ID = "+ cursor.getString(0) +  "User Name= " + cursor.getString(1) + " - Email = "
                                + cursor.getString(2) + " - Password = " + cursor.getString(3) + " - Image " +
                                cursor.getBlob(4));
                        id = Integer.parseInt(cursor.getString(0));
                    }
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("user_id", id);
                    editor.apply();

                    Log.i("id check","id = " + id);

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);

                }
            }
        });
    }


}