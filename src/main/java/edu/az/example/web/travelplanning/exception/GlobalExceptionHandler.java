package edu.az.example.web.travelplanning.exception;

import edu.az.example.web.travelplanning.exception.custom.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({
            AddressNotFoundException.class,
            TripNotFoundException.class,
            UserNotFoundException.class,
            ReviewNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(
            Exception e, HttpServletRequest request) {

        log.warn("Entity not found: {}", e.getMessage());
        return buildErrorResponse(e, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        String errorMessage = "Validation failed: " + String.join(", ", errors);
        log.warn("Validation error: {}", errorMessage);

        return buildErrorResponse(new Exception(errorMessage), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({
            ValidationException.class,
            EmailAlreadyExistsException.class,
            ConfirmPasswordException.class,
            RoleNotFoundException.class,
            IllegalArgumentException.class,
            MultipleReviewCreationException.class})
    public ResponseEntity<ErrorResponse> handleBusinessExceptions(
            Exception e, HttpServletRequest request) {

        log.warn("Business rule violation: {}", e.getMessage());
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorResponse> handleSecurityException(
            SecurityException e, HttpServletRequest request) {

        log.warn("Security exception: {}", e.getMessage());
        return buildErrorResponse(e, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception e, HttpServletRequest request) {

        log.error("Unexpected error occurred: ", e);
        return buildErrorResponse(
                new Exception("Internal server error"),
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            Exception e, HttpStatus status, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(),
                status,
                System.currentTimeMillis(),
                request.getRequestURI(),
                null
        );
        return new ResponseEntity<>(errorResponse, status);
    }
}
