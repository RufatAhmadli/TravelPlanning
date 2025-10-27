package edu.az.example.web.travelplanning.controller;

import edu.az.example.web.travelplanning.dto.TripDto;
import edu.az.example.web.travelplanning.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/trips")
public class TripController {
    private final TripService tripService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public List<TripDto> getAll() {
        return tripService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TripDto getById(@PathVariable Long id) {
        return tripService.findById(id);
    }

    @GetMapping("/destination/{destination}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public List<TripDto> getByDestination(@PathVariable String destination) {
        return tripService.findAllByDestination(destination);
    }

    @GetMapping("/time/{departureTime}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public List<TripDto> getByDepartureTime(@PathVariable LocalDate departureTime) {
        return tripService.findAllByDepartureTime(departureTime);
    }

    @GetMapping("users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public List<TripDto> getByUserId(@PathVariable Long userId) {
        return tripService.findAllByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TripDto createTrip(@RequestBody TripDto tripDto) {
        return tripService.create(tripDto);
    }

    @PostMapping("/{tripId}/addTrip/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public TripDto addTrip(@PathVariable Long tripId, @PathVariable Long userId) {
        return tripService.addTripToUser(tripId, userId);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TripDto updateTrip(@PathVariable Long id, @RequestBody TripDto tripDto) {
        return tripService.update(id, tripDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrip(@PathVariable Long id) {
        tripService.delete(id);
    }
}
