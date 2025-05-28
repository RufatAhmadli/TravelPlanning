package edu.az.example.web.travelplanning.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.az.example.web.travelplanning.enums.GENDER;
import jakarta.validation.constraints.*;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    @Null
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
    private GENDER gender;

    @NotBlank(groups = OnCreate.class)
    private String password;

}
