package com.example;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class WeatherIntegrationTest {

    @Test
    public void shouldSuccessfullyReturnResponse() {
        given()
                .queryParam("lat", 37.7749)
                .queryParam("lon", -122.4194)
                .when().get("/weather")
                .then()
                .statusCode(200)
                .body("size()", is(4))
                .body("location", is("San Francisco"))
                .body("weather_description", anything("clouds"))
                .body("additionalInfo", is("Consider staying at home."));
    }
}
