package com.example.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WeatherResponse {

    private String location;
    private double temperature;
    private String weatherDescription;
    private String additionalInfo;
}
