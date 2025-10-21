package edu.az.example.web.travelplanning.exception.custom;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}
