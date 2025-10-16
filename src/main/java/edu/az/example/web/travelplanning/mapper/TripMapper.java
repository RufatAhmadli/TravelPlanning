package edu.az.example.web.travelplanning.mapper;

import edu.az.example.web.travelplanning.dto.TripDto;
import edu.az.example.web.travelplanning.model.entity.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface TripMapper {

    TripDto toTripDto(Trip trip);

    @Mapping(target = "users", ignore = true)
    Trip toTripEntity(TripDto tripDto);

    @Mapping(target = "users", ignore = true)
    void updateTripEntity(@MappingTarget Trip trip, TripDto tripDto);
}