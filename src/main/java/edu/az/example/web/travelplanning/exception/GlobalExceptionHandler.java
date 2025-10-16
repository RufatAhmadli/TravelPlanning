package edu.az.example.web.travelplanning.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.management.relation.RoleNotFoundException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(Exception e, HttpServletRequest request) {
        Map<String, Object> error = new HashMap<>();
        buildResponse(error, e.getMessage(), HttpStatus.NOT_FOUND, request);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ValidationException.class,
            EmailAlreadyExistsException.class,
            ConfirmPasswordException.class,
            RoleNotFoundException.class,
            MethodArgumentNotValidException.class,}
    )
    public ResponseEntity<?> handleValidationException(Exception e, HttpServletRequest request) {
        Map<String, Object> error = new HashMap<>();
        buildResponse(error, e.getMessage(), HttpStatus.BAD_REQUEST, request);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<?> handleSecurityException(SecurityException e, HttpServletRequest request) {
        Map<String, Object> error = new HashMap<>();
        buildResponse(error, e.getMessage(), HttpStatus.UNAUTHORIZED, request);
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    public void buildResponse(Map<String, Object> error, String message, HttpStatus status, HttpServletRequest request) {
        error.put("message", message);
        error.put("status", status);
        error.put("timestamp", System.currentTimeMillis());
        error.put("path", request.getRequestURI());
    }
}
