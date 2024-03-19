package com.example.service;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/weather")
@RegisterRestClient(baseUri = "https://api.openweathermap.org/data/2.5")
public interface WeatherClient {

    @GET
    String getWeather(@QueryParam("lat") double latitude,
                      @QueryParam("lon") double longitude,
                      @QueryParam("appid") String apiKey);
}
