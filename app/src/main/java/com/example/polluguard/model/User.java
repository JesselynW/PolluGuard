package com.example.polluguard.model;

import android.graphics.Bitmap;

public class User {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private int volunteerPoints;
    private String dateOfBirth;
    private Bitmap image;

    public User(String name, String email, String password, String phoneNumber, int volunteerPoints, Bitmap image) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.volunteerPoints = volunteerPoints;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getVolunteerPoints() {
        return volunteerPoints;
    }

    public void setVolunteerPoints(int volunteerPoints) {
        this.volunteerPoints = volunteerPoints;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
