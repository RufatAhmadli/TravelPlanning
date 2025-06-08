package edu.az.example.web.travelplanning.model.dto;

import edu.az.example.web.travelplanning.enums.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    private Long id;

    @NotBlank
    private String city;

    @NotBlank
    private String street;

    @NotBlank
    private String streetNumber;

    @NotBlank
    private String postalCode;

    @NotNull
    private AddressType type;

    @NotNull
    private UserDto user;
}
