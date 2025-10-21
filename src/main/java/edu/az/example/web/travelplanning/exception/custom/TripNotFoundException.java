package edu.az.example.web.travelplanning.exception.custom;

public class TripNotFoundException extends RuntimeException{

    public TripNotFoundException(Long tripId){
        super("Trip with id " + tripId + " not found");
    }
}
