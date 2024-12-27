package com.example.polluguard.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

//import com.example.polluguard.DB;
import com.example.polluguard.DBHelper;
import com.example.polluguard.model.User;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<User> userData;
    private DBHelper db;
    private SharedPreferences sp;

    public HomeViewModel() {

        userData = new MutableLiveData<>();


    }

    public void initialize(Context context) {
        db = new DBHelper(context);
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
        Log.d("HomeViewModel", "user_id: " + id);
        if (id != -1) {
            User user = db.getUserById(id);
            if (user != null) {
                userData.setValue(user);
                Log.d("HomeViewModel", "User found: " + user.getName());
            } else {
                Log.d("HomeViewModel", "User not found");
                userData.setValue(null);
            }
        } else {
            Log.d("HomeViewModel", "No user_id found in SharedPreferences");
            userData.setValue(null); // Handle case where no user_id is found
        }
        return userData;
    }
}