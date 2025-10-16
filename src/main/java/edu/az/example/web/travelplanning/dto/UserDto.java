package edu.az.example.web.travelplanning.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.az.example.web.travelplanning.enums.Gender;
import edu.az.example.web.travelplanning.validation.OnCreate;
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

    @NotNull
    @Size(min = 3, max = 50)
    private String firstName;

    @NotNull
    @Size(min = 3, max = 50)
    private String lastName;

    @NotNull
    private Integer age;

    @NotNull
    @Email
    private String email;

    @NotNull
    private Gender gender;

    @NotBlank(groups = OnCreate.class)
    private String password;

    private List<AddressDto> addresses;
    private List<TripDto> trips;
}
