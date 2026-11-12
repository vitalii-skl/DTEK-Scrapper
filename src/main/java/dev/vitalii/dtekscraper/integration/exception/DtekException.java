package dev.vitalii.dtekscraper.integration.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DtekException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String message;


    public DtekException(int statusCode, String message) {
        this.httpStatus = HttpStatus.resolve(statusCode);
        this.message = message;
    }
}
