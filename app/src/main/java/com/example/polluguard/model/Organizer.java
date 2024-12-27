package com.example.polluguard.model;

public class Organizer {
    private String organizerName;
    private int organizerLogo;
    private String organizerDesc;

    public Organizer(int organizerLogo) {
        this.organizerLogo = organizerLogo;
    }

    public Organizer(String organizerName, int organizerLogo, String organizerDesc) {
        this.organizerName = organizerName;
        this.organizerLogo = organizerLogo;
        this.organizerDesc = organizerDesc;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public int getOrganizerLogo() {
        return organizerLogo;
    }

    public void setOrganizerLogo(int organizerLogo) {
        this.organizerLogo = organizerLogo;
    }

    public String getOrganizerDesc() {
        return organizerDesc;
    }

    public void setOrganizerDesc(String organizerDesc) {
        this.organizerDesc = organizerDesc;
    }
}
