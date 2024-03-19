package com.example.controller;

import com.example.model.WeatherData;
import com.example.service.WeatherService;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("/weather")
@Singleton
public class WeatherController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherController.class);

    private final WeatherService weatherService;

    public WeatherController(final WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public WeatherData getWeatherData(@QueryParam("lat") String latitudeParam, @QueryParam("lon") String longitudeParam) {
        final double latitude;
        final double longitude;
        try {
            latitude = Double.parseDouble(latitudeParam);
            longitude = Double.parseDouble(longitudeParam);
        } catch (Exception e) {
            LOGGER.error("Invalid latitude value: {} or longitude value: {}", latitudeParam, longitudeParam);
            throw new WebApplicationException("Invalid latitude or longitude value", Response.Status.BAD_REQUEST);
        }
        return weatherService.getWeather(latitude, longitude);
    }
}
