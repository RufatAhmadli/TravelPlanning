package edu.az.example.web.travelplanning.mapper;

import edu.az.example.web.travelplanning.dto.ReviewDto;
import edu.az.example.web.travelplanning.dto.TripDto;
import edu.az.example.web.travelplanning.model.entity.Trip;
import edu.az.example.web.travelplanning.model.entity.TripReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper
public interface TripMapper {

    @Mapping(target = "reviews", source = "tripReviews",
            qualifiedByName = "reviewsWithoutTripAndUser")
    TripDto toTripDto(Trip trip);

    @Mapping(target = "tripReviews", source = "reviews")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "users", ignore = true)
    Trip toTripEntity(TripDto tripDto);

    @Mapping(target = "tripReviews", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateTripEntity(@MappingTarget Trip trip, TripDto tripDto);

    @Named("reviewsWithoutTripAndUser")
    @Mapping(target = "trip", ignore = true)
    @Mapping(target = "user", ignore = true)
    ReviewDto reviewWithoutTripAndUser(TripReview tripReview);
}