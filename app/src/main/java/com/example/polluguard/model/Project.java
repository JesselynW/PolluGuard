package com.example.polluguard.model;

import java.io.Serializable;

public class Project implements Serializable {
    private int projectId;
    private String projectName;
    private int imageProject;
    private String date;
    private String time;
    private String location;
    private double latitude;
    private double longtitude;
    private String about;
    private int reward;
    private int slot;
    private String linkWhatsapp;
    private int whatsappQR;
    private Organizer organizer;

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getImageProject() {
        return imageProject;
    }

    public void setImageProject(int imageProject) {
        this.imageProject = imageProject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public Organizer getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }

    public String getLinkWA() {
        return linkWhatsapp;
    }

    public void setLinkWA(String linkWA) {
        this.linkWhatsapp = linkWA;
    }

    public int getQr() {
        return whatsappQR;
    }

    public void setQr(int qr) {
        this.whatsappQR = qr;
    }
}
