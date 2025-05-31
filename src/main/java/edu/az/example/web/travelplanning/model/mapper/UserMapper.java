package edu.az.example.web.travelplanning.model.mapper;
import edu.az.example.web.travelplanning.model.dto.UserDto;
import edu.az.example.web.travelplanning.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = AddressMapper.class)
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "firstName", source = "name")
    UserDto toUserDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "firstName")
    @Mapping(target = "addresses", source = "addresses")
    User toUser(UserDto userDto);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "firstName")
    @Mapping(target = "addresses", source = "addresses")
    void toUserEntity(@MappingTarget User user, UserDto userDto);
}