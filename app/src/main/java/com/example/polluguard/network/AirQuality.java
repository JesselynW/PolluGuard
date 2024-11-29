package com.example.polluguard.network;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;

public class AirQuality {
    @SerializedName("lat")
    private double lat;

    @SerializedName("lon")
    private double lon;

    @SerializedName("aqi")
    private double aqi;

    @SerializedName("station")
    private Station station;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getAqi() {
        return aqi;
    }

    public void setAqi(double aqi) {
        this.aqi = aqi;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public static class Station {

        public Station(String name) {
            this.name = name;
        }

        @SerializedName("name")
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class AirQualityDeserializer implements JsonDeserializer<AirQuality> {
        @Override
        public AirQuality deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            AirQuality airQuality = new AirQuality();

            String aqiString = jsonObject.get("aqi").getAsString();
            double aqi = -1;

            String latString = jsonObject.get("lat").getAsString();
            double lat = -1;

            String lonString = jsonObject.get("lon").getAsString();
            double lon = -1;

            JsonObject stationObject = jsonObject.get("station").getAsJsonObject();
            Station station = new Station("TAK DAAAAA");;

            if(stationObject != null){
                try {
                    station.setName(stationObject.get("name").getAsString());
                }catch(NumberFormatException e) {
                    station.setName("tak tahu");
                }
            }

            if (aqiString != null && !aqiString.equals("-")) {
                try {
                    aqi = Double.parseDouble(aqiString);
                } catch (NumberFormatException e) {
                    // Jika parsing gagal, kita set ke nilai default
                    aqi = -1;
                }
            }

            if (latString != null && !latString.equals("-")) {
                try {
                    lat = Double.parseDouble(latString);
                } catch (NumberFormatException e) {
                    // Jika parsing gagal, kita set ke nilai default
                    lat = -1;
                }
            }

            if(lonString != null && !lonString.equals("-")){
                try {
                    lon = Double.parseDouble(lonString);
                } catch (NumberFormatException e) {
                    // Jika parsing gagal, kita set ke nilai default
                    lon = -1;
                }
            }

            // Set nilai AQI pada objek AirQuality
            airQuality.setAqi(aqi);
            airQuality.setLat(lat);
            airQuality.setLon(lon);
            airQuality.setStation(station);

            return airQuality;
        }
    }
}
