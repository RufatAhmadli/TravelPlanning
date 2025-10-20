package edu.az.example.web.travelplanning.mapper;

import edu.az.example.web.travelplanning.dto.ReviewDto;
import edu.az.example.web.travelplanning.model.entity.TripReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ReviewMapper {

    @Mapping(target = "trip", ignore = true)
    @Mapping(target = "user", ignore = true)
    ReviewDto toReviewDto(TripReview tripReview);

    @Mapping(target = "trip", ignore = true)
    @Mapping(target = "user", ignore = true)
    TripReview toReviewEntity(ReviewDto reviewDto);
}
