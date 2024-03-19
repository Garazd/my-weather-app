package com.example.service;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@QuarkusTest
public class WeatherClientTest {

    @TestHTTPEndpoint(WeatherClient.class)
    @TestHTTPResource
    URL url;

    @Test
    public void shouldSuccessfullyReturnResponse() throws IOException {
        try (final InputStream in = url.openStream()) {
            final String contents = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            assertThat(contents).isNotBlank();
        }
    }
}
