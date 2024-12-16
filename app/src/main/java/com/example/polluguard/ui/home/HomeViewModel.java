package com.example.polluguard.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.polluguard.DBHelper;
import com.example.polluguard.model.User;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<User> userData;
    private DBHelper dbHelper;
    private SharedPreferences sp;

    public HomeViewModel() {

        userData = new MutableLiveData<>();


    }

    public void initialize(Context context) {
        dbHelper = new DBHelper(context);
        sp = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
    }

//    public void getUser(int id) {
//        User user = dbHelper.getUserById(id);
//        userData.setValue(user);
//    }

    public LiveData<User> getUserLiveData() {
//        int id = sp.getInt("user_id", -1);
//        User user = dbHelper.getUserById(id);
//        userData.setValue(user);
//        return userData;
        int id = sp.getInt("user_id", -1);
        Log.d("HomeViewModel", "user_id: " + id); // Cek apakah ID yang diambil benar
        if (id != -1) {
            User user = dbHelper.getUserById(id);
            if (user != null) {
                userData.setValue(user); // Update LiveData
                Log.d("HomeViewModel", "User found: " + user.getName()); // Cek apakah user ditemukan
            } else {
                Log.d("HomeViewModel", "User not found");
                userData.setValue(null); // Handle if user is not found
            }
        } else {
            Log.d("HomeViewModel", "No user_id found in SharedPreferences");
            userData.setValue(null); // Handle case where no user_id is found
        }
        return userData;
    }
}