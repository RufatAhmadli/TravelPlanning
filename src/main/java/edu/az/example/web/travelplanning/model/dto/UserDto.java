package edu.az.example.web.travelplanning.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.az.example.web.travelplanning.enums.Gender;
import edu.az.example.web.travelplanning.validation.OnCreate;
import edu.az.example.web.travelplanning.validation.OnRegister;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    @Null(groups = OnCreate.class)
    private Long id;

    @NotNull(groups = {OnCreate.class, OnRegister.class})
    @Size(min = 3, max = 50)
    private String firstName;

    @NotNull(groups = {OnCreate.class, OnRegister.class})
    @Size(min = 3, max = 50)
    private String lastName;

    @Min(18)
    @NotNull(groups = {OnCreate.class, OnRegister.class})
    private Integer age;

    @NotNull(groups = {OnCreate.class, OnRegister.class})
    @Email
    private String email;

    @NotNull(groups = {OnCreate.class, OnRegister.class})
    private Gender gender;

    @NotBlank(groups = {OnCreate.class, OnRegister.class})
    private String password;

    @NotBlank(groups = OnRegister.class)
    private String confirmPassword;

    private List<AddressDto> addresses;
    private List<TripDto> trips;
}
