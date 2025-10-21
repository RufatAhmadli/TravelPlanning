package edu.az.example.web.travelplanning.exception.custom;


public class AddressNotFoundException extends RuntimeException{
    public AddressNotFoundException(Long id) {
        super("Could not find address with id " + id);
    }
}
