package edu.az.example.web.travelplanning.exception;

public class ConfirmPasswordException extends RuntimeException {
    public ConfirmPasswordException() {
        super("Passwords do not match");
    }
}
