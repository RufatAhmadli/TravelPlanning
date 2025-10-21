package edu.az.example.web.travelplanning.exception.custom;

public class MultipleReviewCreationException extends RuntimeException {

    public MultipleReviewCreationException() {
        super("One user can't create multiple reviews about a trip");
    }
}