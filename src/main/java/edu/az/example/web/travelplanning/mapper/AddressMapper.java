package edu.az.example.web.travelplanning.mapper;

import edu.az.example.web.travelplanning.dto.AddressDto;
import edu.az.example.web.travelplanning.model.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TripMapper.class})
public interface AddressMapper {

    @Mapping(target = "type", source = "addressType")
    AddressDto toAddressDto(Address address);


    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addressType", source = "type")
    Address toAddressEntity(AddressDto addressDto);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addressType", source = "type")
    void updateAddressEntity(@MappingTarget Address address, AddressDto addressDto);

    List<AddressDto> toDtoList(List<Address> addresses);
}

