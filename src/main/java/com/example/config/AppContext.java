package com.example.config;

import com.example.service.WeatherClient;
import com.example.controller.WeatherController;
import com.example.service.WeatherService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class AppContext {

    @Produces
    public WeatherController weatherResource(@RestClient WeatherClient weatherClient) {
        final WeatherService weatherService = new WeatherService(weatherClient, new WeatherConfig());
        return new WeatherController(weatherService);
    }
}
