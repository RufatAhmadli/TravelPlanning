package edu.az.example.web.travelplanning.mapper;

import edu.az.example.web.travelplanning.model.dto.AddressDto;
import edu.az.example.web.travelplanning.model.dto.UserDto;
import edu.az.example.web.travelplanning.model.entity.Address;
import edu.az.example.web.travelplanning.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring",uses = {TripMapper.class})
public interface AddressMapper {

    @Mapping(target = "user", qualifiedByName = "userWithoutTripsAndAddresses")
    @Mapping(target = "type", source = "addressType")
    AddressDto toAddressDto(Address address);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addressType", source = "type")
    Address toAddress(AddressDto addressDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addressType", source = "type")
    void toAddressEntity(@MappingTarget Address address, AddressDto addressDto);

    List<AddressDto> toDtoList(List<Address> addresses);
}

