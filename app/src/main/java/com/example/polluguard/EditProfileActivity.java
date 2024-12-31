package com.example.polluguard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.polluguard.model.User;
import com.example.polluguard.ui.profile.ProfileFragment;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etName, etEmail,etOldPw, etNewPw,etPhoneNumber;
    private ImageView backButton, ivProfile;

    private Button submitButton;

    private User user;

    private Uri uri;
    private Bitmap bitmapImage;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        etName = findViewById(R.id.nameEditText);
        etEmail = findViewById(R.id.emailEditText);
        etOldPw = findViewById(R.id.oldPwEditText);
        etNewPw = findViewById(R.id.newPwEditText);
        etPhoneNumber = findViewById(R.id.phoneNumberEditText);

        ivProfile = findViewById(R.id.image);
        submitButton = findViewById(R.id.submitButton);
        backButton = findViewById(R.id.backButton);

        sp = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        int id = sp.getInt("user_id", -1);
        DBHelper dbHelper = new DBHelper(this);

        loadUserData(dbHelper, id);

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
                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                }
            }
        });

        submitButton.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            String oldPw = etOldPw.getText().toString();
            String newPw = etNewPw.getText().toString();
            String phoneNumber = etPhoneNumber.getText().toString();

            dbHelper.validateProfile(this, id, name, email, oldPw, newPw, phoneNumber, bitmapImage);
//            if(isValid) {
//                Fragment fragment = new ProfileFragment();
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.nav_host_fragment_activity_home, fragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
//            }
            loadUserData(dbHelper, id);
        });

        backButton.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    public void loadUserData(DBHelper dbHelper, int id){
        user = dbHelper.getUserById(id);

        if(user != null){
            ivProfile.setImageBitmap(user.getImage());
            etName.setText(user.getName());
            etEmail.setText(user.getEmail());
            etPhoneNumber.setText(user.getPhoneNumber()); // phone number nya masih gak bisa
            Log.i("gaada kah? = ", "test = " + user.getPhoneNumber());
        }
    }

}