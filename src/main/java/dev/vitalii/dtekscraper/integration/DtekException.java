package dev.vitalii.dtekscraper.integration;

public class DtekException extends RuntimeException {

    public DtekException(int statusCode, String message) {
        super("API request failed with HTTP " + statusCode + ": " + message);
    }
}
