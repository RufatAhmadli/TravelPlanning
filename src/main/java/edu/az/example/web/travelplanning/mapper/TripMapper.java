package edu.az.example.web.travelplanning.mapper;

import edu.az.example.web.travelplanning.model.dto.TripDto;
import edu.az.example.web.travelplanning.model.entity.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TripMapper {
    TripMapper INSTANCE = Mappers.getMapper(TripMapper.class);

    TripDto toTripDto(Trip trip);

    Trip toTrip(TripDto tripDto);

    void toTripEntity(@MappingTarget Trip trip, TripDto tripDto);

    List<TripDto> toTripDtoList(List<Trip> tripList);
    List<Trip>  toTripList(List<TripDto> tripDtoList);
}
