package edu.az.example.web.travelplanning.mapper;

import edu.az.example.web.travelplanning.dto.UserDto;
import edu.az.example.web.travelplanning.dto.UserPatchDto;
import edu.az.example.web.travelplanning.model.entity.Address;
import edu.az.example.web.travelplanning.model.entity.User;
import org.mapstruct.*;


@Mapper(uses = {TripMapper.class, AddressMapper.class, ReviewMapper.class})
public interface UserMapper {
    @Mapping(target = "confirmPassword", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "firstName", source = "name")
    @Mapping(target = "reviews", qualifiedByName = "reviewsWithoutTripAndUser")
    UserDto toUserDto(User user);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "firstName")
    User toUserEntity(UserDto userDto);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "firstName")
    void updateUserEntity(@MappingTarget User user, UserDto userDto);

    default void applyPatch(@MappingTarget User user, UserPatchDto patchDto) {
        if (patchDto.getFirstName() != null) {
            user.setName(patchDto.getFirstName());
        }
        if (patchDto.getLastName() != null) {
            user.setLastName(patchDto.getLastName());
        }
        if (patchDto.getAge() != null) {
            user.setAge(patchDto.getAge());
        }
        if (patchDto.getEmail() != null) {
            if (isValidEmail(patchDto.getEmail())) {
                user.setEmail(patchDto.getEmail());
            } else {
                throw new IllegalArgumentException("Invalid email format");
            }
        }
    }

    default boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }


    @AfterMapping
    default void linkAddresses(@MappingTarget User user) {
        if (user.getAddresses() != null) {
            for (Address address : user.getAddresses()) {
                address.setUser(user);
            }
        }
    }
}