package com.example.exception;

import java.io.Serial;

public class WeatherException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1701735030277484108L;

    public WeatherException(String message) {
        super(message);
    }
}
