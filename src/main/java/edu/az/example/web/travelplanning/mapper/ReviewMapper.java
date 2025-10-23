package edu.az.example.web.travelplanning.mapper;

import edu.az.example.web.travelplanning.dto.ReviewDto;
import edu.az.example.web.travelplanning.dto.TripDto;
import edu.az.example.web.travelplanning.dto.UserDto;
import edu.az.example.web.travelplanning.model.entity.Trip;
import edu.az.example.web.travelplanning.model.entity.TripReview;
import edu.az.example.web.travelplanning.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper
public interface ReviewMapper {

    @Mapping(target = "trip", qualifiedByName = "tripWithoutRelations")
    @Mapping(target = "user", qualifiedByName = "userWithoutRelations")
    ReviewDto toReviewDto(TripReview tripReview);

    @Mapping(target = "trip", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    TripReview toReviewEntity(ReviewDto reviewDto);

    @Mapping(target = "firstName", source = "name")
    @Mapping(target = "confirmPassword", ignore = true)
    @Mapping(target = "trips", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Named("userWithoutRelations")
    UserDto userWithoutAddressesAndTrips(User user);

    @Mapping(target = "reviews", ignore = true)
    @Named("tripWithoutRelations")
    TripDto mapTripWithoutRelations(Trip trip);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "trip", ignore = true)
    @Mapping(target = "id", ignore = true)
    TripReview updateReview(@MappingTarget TripReview tripReview, ReviewDto reviewDto);
}

