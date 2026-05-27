package com.airasia.lowfare.exception;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
        MissingServletRequestParameterException.class
    )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMissingParam(
        MissingServletRequestParameterException ex
    ) {
        return Map.of(
            "error",
            ex.getParameterName() + " is required"
        );
    }

    @ExceptionHandler(
        ConstraintViolationException.class
    )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(
        ConstraintViolationException ex
    ) {
        return Map.of(
            "error",
            ex.getMessage()
        );
    }
}