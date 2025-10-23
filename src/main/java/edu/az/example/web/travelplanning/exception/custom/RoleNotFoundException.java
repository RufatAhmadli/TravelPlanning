package edu.az.example.web.travelplanning.exception.custom;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String message) {
        super(message);
    }

    public RoleNotFoundException(Long id) {
        super("Role with id " + id + " not found");
    }
}
