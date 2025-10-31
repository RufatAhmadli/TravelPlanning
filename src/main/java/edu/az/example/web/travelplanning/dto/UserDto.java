package edu.az.example.web.travelplanning.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.az.example.web.travelplanning.enums.Gender;
import edu.az.example.web.travelplanning.validation.OnCreate;
import edu.az.example.web.travelplanning.validation.OnRegister;
import edu.az.example.web.travelplanning.validation.OnUpdate;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    @Null(groups = OnCreate.class)
    private Long id;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class, OnRegister.class})
    @Size(min = 3, max = 50)
    private String firstName;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class, OnRegister.class})
    @Size(min = 3, max = 50)
    private String lastName;

    @Min(value = 18, groups = {OnCreate.class, OnUpdate.class, OnRegister.class})
    @NotNull(groups = {OnCreate.class, OnRegister.class})
    private Integer age;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class, OnRegister.class})
    @Email
    private String email;

    @NotNull(groups = {OnCreate.class, OnUpdate.class, OnRegister.class})
    private Gender gender;

    @NotBlank(groups = {OnCreate.class, OnRegister.class})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank(groups = OnRegister.class)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String confirmPassword;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private RoleDto role;

    private List<AddressDto> addresses;
    private List<TripDto> trips;
    private List<ReviewDto> reviews;
}
