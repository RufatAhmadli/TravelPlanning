package edu.az.example.web.travelplanning.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.az.example.web.travelplanning.validation.OnCreate;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TripDto {
    @Null(groups = OnCreate.class)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 30)
    private String destination;

    @NotBlank
    @Size(min = 2, max = 30)
    private String departure;

    @NotNull
    @FutureOrPresent
    private LocalDate departureTime;

    @NotNull
    @Future
    private LocalDate arrivalTime;

    private String description;

    private List<ReviewDto> reviews;
}
