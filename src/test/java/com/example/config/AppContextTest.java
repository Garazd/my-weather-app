package com.example.config;

import com.example.controller.WeatherController;
import com.example.service.WeatherClient;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class AppContextTest {

    @Test
    public void testWeatherResource() {
        final WeatherClient weatherClient = mock(WeatherClient.class);

        final AppContext appContext = new AppContext();
        final WeatherController weatherController = appContext.weatherResource(weatherClient);

        assertThat(weatherController).isNotNull();
    }
}
