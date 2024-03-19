package com.example.service;

import com.example.config.WeatherConfig;
import com.example.exception.WeatherException;
import com.example.model.WeatherData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logmanager.Level;

import java.util.logging.Logger;

@Singleton
public class WeatherService {
    private final Logger LOGGER = Logger.getLogger("com.example.service.WeatherService");
    private static final String MAIN_NODE = "main";
    private static final String TEMP_NODE = "temp";
    private final WeatherClient weatherClient;
    private final WeatherConfig weatherConfig;

    public WeatherService(@RestClient WeatherClient weatherClient, WeatherConfig weatherConfig) {
        this.weatherClient = weatherClient;
        this.weatherConfig = weatherConfig;
    }

    @Nullable
    public WeatherData getWeather(double latitude, double longitude) {
        try {
            LOGGER.log(Level.INFO, ("Called weatherService with lat: " + latitude + " lon: " + longitude));
            final String weatherData = weatherClient.getWeather(latitude, longitude, weatherConfig.getApiKey());
            return parseAndTransformWeatherData(weatherData);
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "Error fetching weather data: " + e.getMessage());
            throw new WeatherException("Error fetching weather data");
        }
    }

    @Nullable
    private WeatherData parseAndTransformWeatherData(String weatherData) {
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            final JsonNode jsonNode = objectMapper.readTree(weatherData);
            final WeatherData transformedData = new WeatherData();
            transformedData.setLocation(jsonNode.get("name").asText());
            transformedData.setTemperature(jsonNode.get(MAIN_NODE).get(TEMP_NODE).asDouble());
            transformedData.setWeatherDescription(jsonNode.get("weather").get(0).get("description").asText());
            final double temperature = jsonNode.get(MAIN_NODE).get(TEMP_NODE).asDouble();
            final double humidity = jsonNode.get(MAIN_NODE).get("humidity").asDouble();
            final String additionalInfo = getAdditionalInfo(temperature, humidity);
            transformedData.setAdditionalInfo(additionalInfo);
            return transformedData;
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.ERROR, "Error fetching weather data: " + e.getMessage());
            return null;
        }
    }

    private String getAdditionalInfo(double temperature, double humidity) {
        if (temperature >= 20 && temperature <= 30 && humidity <= 70) {
            return "Good weather for a walk.";
        } else if (temperature > 30 || (temperature >= 20 && humidity > 70)) {
            return "Consider staying at home.";
        } else {
            return "Weather conditions are normal.";
        }
    }
}
