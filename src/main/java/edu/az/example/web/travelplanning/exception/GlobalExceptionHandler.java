package edu.az.example.web.travelplanning.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException e, HttpServletRequest request) {
        Map<String, Object> error = new HashMap<>();
        buildResponse(error, e.getMessage(), HttpStatus.NOT_FOUND, request);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException e, HttpServletRequest request) {
        Map<String, Object> error = new HashMap<>();
        buildResponse(error, e.getMessage(), HttpStatus.BAD_REQUEST, request);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    public void buildResponse(Map<String, Object> error, String message, HttpStatus status, HttpServletRequest request) {
        error.put("message", message);
        error.put("status", status);
        error.put("timestamp", System.currentTimeMillis());
        error.put("path", request.getRequestURI());
    }
}
