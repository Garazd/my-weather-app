package com.example.service;

import com.example.config.WeatherConfig;
import com.example.exception.WeatherException;
import com.example.model.CityWeather;
import com.example.response.WeatherResponse;
import jakarta.annotation.Nullable;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logmanager.Level;

import java.util.logging.Logger;

@Singleton
public class WeatherService {
    private final Logger LOGGER = Logger.getLogger("com.example.service.WeatherService");
    private final WeatherClient weatherClient;
    private final WeatherConfig weatherConfig;

    public WeatherService(@RestClient WeatherClient weatherClient, WeatherConfig weatherConfig) {
        this.weatherClient = weatherClient;
        this.weatherConfig = weatherConfig;
    }

    @Nullable
    public WeatherResponse getWeather(double latitude, double longitude) {
        try {
            LOGGER.log(Level.INFO, ("Called weatherService with lat: " + latitude + " lon: " + longitude));
            final CityWeather weatherData = weatherClient.getWeather(latitude, longitude, weatherConfig.getApiKey());
            return parseAndTransformWeatherData(weatherData);
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "Error fetching weather data: " + e.getMessage());
            throw new WeatherException("Error fetching weather data");
        }
    }

    @Nullable
    private WeatherResponse parseAndTransformWeatherData(CityWeather cityWeather) {
        try {
            final WeatherResponse transformedData = new WeatherResponse();
            transformedData.setLocation(cityWeather.getName());
            transformedData.setTemperature(cityWeather.getMain().getTemp());
            transformedData.setWeatherDescription(cityWeather.getWeatherList().getFirst().getDescription());
            final double temperature = cityWeather.getMain().getTemp();
            final double humidity = cityWeather.getMain().getHumidity();
            final String additionalInfo = getAdditionalInfo(temperature, humidity);
            transformedData.setAdditionalInfo(additionalInfo);
            return transformedData;
        } catch (Exception e) {
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
