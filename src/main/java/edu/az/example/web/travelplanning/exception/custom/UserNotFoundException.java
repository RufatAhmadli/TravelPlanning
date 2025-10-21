package edu.az.example.web.travelplanning.exception.custom;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("User with id " + id + " not found");
    }
}
