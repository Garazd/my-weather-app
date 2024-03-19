package com.example.controller;

import com.example.exception.WeatherException;
import com.example.response.WeatherResponse;
import com.example.service.WeatherService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@QuarkusTest
public class WeatherControllerTest {

    private static final double LATITUDE = 37.7749;
    private static final double LONGITUDE = -122.4194;
    private static final String LATITUDE_PARAM = "37.7749";
    private static final String LONGITUDE_PARAM = "-122.4194";

    private WeatherController weatherController;
    private WeatherService weatherService;

    @BeforeEach
    public void setup() {
        weatherService = mock(WeatherService.class);
        weatherController = new WeatherController(weatherService);
    }

    @Test
    public void shouldSuccessfullyReturnWeatherData() {
        final WeatherResponse expectedWeatherResponse = new WeatherResponse();
        expectedWeatherResponse.setLocation("San Francisco");
        expectedWeatherResponse.setWeatherDescription("Sunny");
        expectedWeatherResponse.setAdditionalInfo("Good weather for a walk.");
        when(weatherService.getWeather(LATITUDE, LONGITUDE)).thenReturn(expectedWeatherResponse);

        final WeatherResponse weatherResponse = weatherController.getWeatherData(LATITUDE_PARAM, LONGITUDE_PARAM);

        assertThat(expectedWeatherResponse).isEqualTo(weatherResponse);
    }

    @Test
    public void shouldReturnExceptionWhenWeatherServiceReturnException() {
        when(weatherService.getWeather(LATITUDE, LONGITUDE)).thenThrow(new WeatherException("Error fetching weather data"));

        assertThatThrownBy(() -> weatherController.getWeatherData(LATITUDE_PARAM, LONGITUDE_PARAM))
                .isInstanceOf(WeatherException.class)
                .hasMessage("Error fetching weather data");
    }

    @Test
    public void shouldReturnExceptionWhenInvalidParam() {
        assertThatThrownBy(() -> weatherController.getWeatherData("LATITUDE_PARAM", "LONGITUDE_PARAM"))
                .isInstanceOf(WebApplicationException.class)
                .hasMessage("Invalid latitude or longitude value");
    }
}
