package edu.az.example.web.travelplanning.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ErrorResponse(
        String message,
        HttpStatus status,
        long timestamp,
        String path,
        List<String> details
) {}
