package com.example.util;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class LogCaptureHandler extends Handler {

    private final List<String> capturedMessages = new ArrayList<>();

    @Override
    public void publish(final LogRecord record) {
        capturedMessages.add(record.getMessage());
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }

    public List<String> getCapturedMessages() {
        return capturedMessages.stream().toList();
    }
}
