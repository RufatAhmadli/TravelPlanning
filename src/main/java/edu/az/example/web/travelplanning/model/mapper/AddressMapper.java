package edu.az.example.web.travelplanning.model.mapper;

import edu.az.example.web.travelplanning.model.dto.AddressDto;
import edu.az.example.web.travelplanning.model.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface AddressMapper {
    @Mapping(target ="type" ,source = "addressType")
    AddressDto toDto(Address address);

    @Mapping(target = "id", ignore = true)
    @Mapping(target ="addressType" ,source = "type")
    Address toAddress(AddressDto addressDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target ="addressType" ,source = "type")
    void toAddressEntity(@MappingTarget Address address,AddressDto addressDto);
}
