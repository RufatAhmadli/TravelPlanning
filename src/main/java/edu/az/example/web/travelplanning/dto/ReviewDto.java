package edu.az.example.web.travelplanning.dto;

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
public class ReviewDto {
    @Null
    private Long id;

    @NotNull
    private Integer rating;

    private String comment;

    private UserDto user;

    private TripDto trip;
}
