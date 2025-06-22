package edu.az.example.web.travelplanning.mapper;

import edu.az.example.web.travelplanning.model.dto.TripDto;
import edu.az.example.web.travelplanning.model.dto.UserDto;
import edu.az.example.web.travelplanning.model.entity.Trip;
import edu.az.example.web.travelplanning.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;


@Mapper
public interface TripMapper {

    @Mapping(target = "users", qualifiedByName = "userWithoutTripsAndAddresses")
    TripDto toTripDto(Trip trip);

    @Named("userWithoutTripsAndAddresses")
    @Mapping(target = "firstName",source = "name")
    @Mapping(target = "trips",ignore = true)
    @Mapping(target = "addresses",ignore = true)
    UserDto userWithoutTrips(User user);

    @Mapping(target = "users", ignore = true)
    Trip toTrip(TripDto tripDto);

    void toTripEntity(@MappingTarget Trip trip, TripDto tripDto);
}