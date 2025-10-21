package edu.az.example.web.travelplanning.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.az.example.web.travelplanning.validation.OnCreate;
import edu.az.example.web.travelplanning.validation.OnUpdate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewDto {
    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private Long id;

    @NotNull(groups = {OnCreate.class,OnUpdate.class})
    private Integer rating;

    private String comment;

    private UserDto user;

    private TripDto trip;
}
