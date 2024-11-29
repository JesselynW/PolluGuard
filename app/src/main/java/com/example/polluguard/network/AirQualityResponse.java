package com.example.polluguard.network;

import java.util.List;

public class AirQualityResponse {
    private String status;    // Optional: for storing the status of the response
    private List<AirQuality> data;  // This is the actual data we're interested in

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AirQuality> getData() {
        return data;
    }

    public void setData(List<AirQuality> data) {
        this.data = data;
    }
}
