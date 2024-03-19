package com.example.service;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;

@QuarkusTest
public class WeatherClientTest {

    @Test
    public void shouldSuccessfullyReturnResponse() {
        given()
                .queryParam("lat", 37.7749)
                .queryParam("lon", -122.4194)
                .when()
                .get("/weather")
                .then()
                .statusCode(200)
                .body(not(emptyOrNullString()));
    }
}
