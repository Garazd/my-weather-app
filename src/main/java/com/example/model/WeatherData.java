package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherData {

    @JsonProperty("location")
    private String location;

    @JsonProperty("temperature")
    private double temperature;

    @JsonProperty("weather_description")
    private String weatherDescription;

    private String additionalInfo;

    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(final double temperature) {
        this.temperature = temperature;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(final String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(final String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
