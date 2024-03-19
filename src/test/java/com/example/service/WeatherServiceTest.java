package com.example.service;

import com.example.config.WeatherConfig;
import com.example.exception.WeatherException;
import com.example.model.CityWeather;
import com.example.model.Main;
import com.example.model.Weather;
import com.example.response.WeatherResponse;
import com.example.util.LogCaptureHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WeatherServiceTest {
    private final Logger logger = Logger.getLogger("com.example.service.WeatherService");
    private final LogCaptureHandler logCaptureHandler = new LogCaptureHandler();
    private static final String ERROR_MESSAGE = "Error fetching weather data";
    private static final double LATITUDE = 37.7749;
    private static final double LONGITUDE = -122.4194;
    private static final String TEST_API_KEY = "testApiKey";
    private static final CityWeather WEATHER = CityWeather.builder()
            .name("San Francisco")
            .weatherList(List.of(Weather.builder().description("Sunny").build()))
            .main(Main.builder().temp(25.0).humidity(50).build())
            .build();

    private WeatherService weatherService;

    @Mock
    private WeatherClient weatherClient;

    @BeforeAll
    public void init() {
        logger.addHandler(logCaptureHandler);
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        final WeatherConfig weatherConfig = new WeatherConfig();
        weatherConfig.setApiKey(TEST_API_KEY);
        weatherService = new WeatherService(weatherClient, weatherConfig);
    }

    @Test
    public void shouldSuccessfullyReturnWeatherDataWithGoodWeather() {
        when(weatherClient.getWeather(LATITUDE, LONGITUDE, TEST_API_KEY)).thenReturn(WEATHER);

        final WeatherResponse weatherData = weatherService.getWeather(LATITUDE, LONGITUDE);

        assertThat(Objects.requireNonNull(weatherData).getLocation()).isEqualTo("San Francisco");
        assertThat(weatherData.getTemperature()).isEqualTo(25.0);
        assertThat(weatherData.getWeatherDescription()).isEqualTo("Sunny");
        assertThat(weatherData.getAdditionalInfo()).isEqualTo("Good weather for a walk.");
    }

    @Test
    public void shouldSuccessfullyReturnWeatherDataWithBadWeather() {
        final Main main = Main.builder().temp(35.0).build();
        WEATHER.setMain(main);

        when(weatherClient.getWeather(LATITUDE, LONGITUDE, TEST_API_KEY)).thenReturn(WEATHER);

        final WeatherResponse weatherData = weatherService.getWeather(LATITUDE, LONGITUDE);

        assertThat(Objects.requireNonNull(weatherData).getLocation()).isEqualTo("San Francisco");
        assertThat(weatherData.getTemperature()).isEqualTo(35.0);
        assertThat(weatherData.getWeatherDescription()).isEqualTo("Sunny");
        assertThat(weatherData.getAdditionalInfo()).isEqualTo("Consider staying at home.");
    }

    @Test
    public void shouldSuccessfullyReturnWeatherDataWithSoSoWeather() {
        final Main main = Main.builder().temp(15.0).humidity(75).build();
        WEATHER.setMain(main);

        when(weatherClient.getWeather(LATITUDE, LONGITUDE, TEST_API_KEY)).thenReturn(WEATHER);

        final WeatherResponse weatherData = weatherService.getWeather(LATITUDE, LONGITUDE);

        assertThat(Objects.requireNonNull(weatherData).getLocation()).isEqualTo("San Francisco");
        assertThat(weatherData.getTemperature()).isEqualTo(15.0);
        assertThat(weatherData.getWeatherDescription()).isEqualTo("Sunny");
        assertThat(weatherData.getAdditionalInfo()).isEqualTo("Weather conditions are normal.");
    }

    @Test
    public void shouldThrowExceptionWhenWeatherClientReturnException() {
        when(weatherClient.getWeather(anyDouble(), anyDouble(), anyString())).thenThrow(new RuntimeException("Client error"));

        assertThatThrownBy(() -> weatherService.getWeather(LATITUDE, LONGITUDE))
                .isInstanceOf(WeatherException.class)
                .hasMessage(ERROR_MESSAGE);
    }

    @Test
    public void shouldReturnEmptyResponseWhenWeatherClientReturnNull() {
        when(weatherClient.getWeather(anyDouble(), anyDouble(), anyString())).thenReturn(null);

        final WeatherResponse weather = weatherService.getWeather(LATITUDE, LONGITUDE);
        assertThat(weather).isNull();
    }

    @Test
    public void shouldReturnEmptyDataWhenWeatherClientReturnBrokenResponse() {
        WEATHER.setMain(null);

        when(weatherClient.getWeather(LATITUDE, LONGITUDE, TEST_API_KEY)).thenReturn(WEATHER);
        final List<String> logs = List.of("Called weatherService with lat: 37.7749 lon: -122.4194",
                "Error fetching weather data: Cannot invoke \"com.example.model.Main.getTemp()\" " +
                        "because the return value of \"com.example.model.CityWeather.getMain()\" is null");

        final WeatherResponse weatherData = weatherService.getWeather(LATITUDE, LONGITUDE);
        final List<String> capturedMessages = logCaptureHandler.getCapturedMessages();

        assertThat(weatherData).isNull();
        assertThat(capturedMessages).containsAll(logs);
    }
}
