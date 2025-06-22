package edu.az.example.web.travelplanning.mapper;
import edu.az.example.web.travelplanning.model.dto.UserDto;
import edu.az.example.web.travelplanning.model.entity.Address;
import edu.az.example.web.travelplanning.model.entity.Trip;
import edu.az.example.web.travelplanning.model.entity.User;
import org.mapstruct.*;

import java.util.ArrayList;

@Mapper(uses = {TripMapper.class,AddressMapper.class})
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "firstName", source = "name")
    UserDto toUserDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "firstName")
    User toUser(UserDto userDto);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "firstName")
    void toUserEntity(@MappingTarget User user, UserDto userDto);


    @AfterMapping
    default void linkAddresses(@MappingTarget User user) {
        if (user.getAddresses() != null) {
            for (Address address : user.getAddresses()) {
                address.setUser(user);
            }
        }
    }
}