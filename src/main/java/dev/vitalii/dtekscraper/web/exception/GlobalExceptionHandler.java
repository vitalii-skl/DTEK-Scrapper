package dev.vitalii.dtekscraper.web.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.core.JacksonException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final ProblemDetail detail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "The request contains invalid parameters");

        final Map<String, String> errors = new HashMap<>();
        e.getBindingResult()
                .getFieldErrors()
                .forEach(error -> {
                    errors.put(error.getField(), error.getDefaultMessage());
                });

        detail.setTitle("Validation Failed");
        detail.setProperty("invalidParams", errors);
        detail.setProperty("timestamp", Instant.now());

        return detail;
    }

    @ExceptionHandler(JacksonException.class)
    public ProblemDetail handleJacksonException(JacksonException e) {
        final ProblemDetail detail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_GATEWAY,
                "Failed to parse data from the external provider."
        );

        detail.setTitle("Upstream Integration Error");
        detail.setProperty("timestamp", Instant.now());

        return detail;
    }
}
