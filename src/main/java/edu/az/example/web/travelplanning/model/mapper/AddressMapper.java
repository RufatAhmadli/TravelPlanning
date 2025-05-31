package edu.az.example.web.travelplanning.model.mapper;

import edu.az.example.web.travelplanning.model.dto.AddressDto;
import edu.az.example.web.travelplanning.model.entity.Address;
import edu.az.example.web.travelplanning.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "type", source = "addressType")
    AddressDto toDto(Address address);

    @Mapping(target = "user", source = "userId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addressType", source = "type")
    Address toAddress(AddressDto addressDto);

    @Mapping(target = "user", source = "userId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addressType", source = "type")
    void toAddressEntity(@MappingTarget Address address, AddressDto addressDto);

    default User map(Long userId) {
        if (userId == null) return null;
        User user = new User();
        user.setId(userId);
        return user;
    }
}
