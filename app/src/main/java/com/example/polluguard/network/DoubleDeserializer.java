package com.example.polluguard.network;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class DoubleDeserializer implements JsonDeserializer<Double> {

    @Override
    public Double deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String value = json.getAsString();

        // Check if the value is "-" or any invalid value and return a default value (e.g., 0.0)
        if ("-".equals(value) || value.isEmpty()) {
            return 0.0;  // Default fallback value
        }

        try {
            // Try to parse the value as a double
            return json.getAsDouble();
        } catch (NumberFormatException e) {
            // If parsing fails, return a default value
            return 0.0;  // Default fallback value
        }
    }
}
