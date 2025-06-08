package edu.az.example.web.travelplanning.model.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class TripDto {

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
    private List<UserDto> users;

    @AssertTrue(message = "Arrival time must be after departure time")
    public boolean isArrivalAfterDeparture() {
        if (arrivalTime == null || departureTime == null) {
            return true;
        }
        return arrivalTime.isAfter(departureTime);
    }


}
