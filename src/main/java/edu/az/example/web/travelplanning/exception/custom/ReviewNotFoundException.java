package edu.az.example.web.travelplanning.exception.custom;

public class ReviewNotFoundException extends RuntimeException {

    public ReviewNotFoundException() {
        super("Review not found");
    }
}
