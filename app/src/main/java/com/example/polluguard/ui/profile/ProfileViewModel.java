package com.example.polluguard.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.polluguard.DBHelper;
import com.example.polluguard.model.Project;
import com.example.polluguard.model.User;

import java.util.ArrayList;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<User> userData;
    private MutableLiveData<ArrayList<Project>> userProjectsData;
    private DBHelper db;
    private SharedPreferences sp;

    public ProfileViewModel() {
        userData = new MutableLiveData<>();
        userProjectsData = new MutableLiveData<>();
    }

    public void initialize(Context context) {
        db = new DBHelper(context);
        sp = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
    }

    public LiveData<User> getUserLiveData() {
        int id = sp.getInt("user_id", -1);
        Log.i("PROFILE VIEW MODEL", "user id = " + id);
        if(id != -1){
            User user = db.getUserById(id);
            if(user != null){
                userData.setValue(user);
            }
            else {
                userData.setValue(null);
            }
        }
        else {
            userData.setValue(null);
        }
        return userData;
    }

    public LiveData<ArrayList<Project>> getUserProjectLiveData() {
        int id = sp.getInt("user_id", -1);
        if(id != -1){
            ArrayList<Project> userProjects = db.getFiveUserProject(id);
            Log.i("PROFILE VIEW MODEL", "ADA GAK = " + userProjects);
            if(userProjects != null){
                userProjectsData.setValue(userProjects);
            }
            else {
                userProjectsData.setValue(null);
            }
        }
        else {
            userProjectsData.setValue(null);
        }
        return userProjectsData;
    }
}