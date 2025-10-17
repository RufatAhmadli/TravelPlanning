package edu.az.example.web.travelplanning.controller;

import edu.az.example.web.travelplanning.dto.TripDto;
import edu.az.example.web.travelplanning.service.TripService;
import lombok.RequiredArgsConstructor;
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
    public List<TripDto> getAll() {
        return tripService.findAll();
    }

    @GetMapping("/{id}")
    public TripDto getById(@PathVariable Long id) {
        return tripService.findById(id);
    }

    @GetMapping("/destination/{destination}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<TripDto> getByDestination(@PathVariable String destination) {
        return tripService.findAllByDestination(destination);
    }

    @GetMapping("/time/{departureTime}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<TripDto> getByDepartureTime(@PathVariable LocalDate departureTime) {
        return tripService.findAllByDepartureTime(departureTime);
    }

    @GetMapping("users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<TripDto> getByUserId(@PathVariable Long userId) {
        return tripService.findAllByUserId(userId);
    }

    @PostMapping
    public TripDto createTrip(@RequestBody TripDto tripDto) {
        return tripService.create(tripDto);
    }

    @PutMapping("/{id}")
    public TripDto updateTrip(@PathVariable Long id, @RequestBody TripDto tripDto) {
        return tripService.update(id, tripDto);
    }

    @DeleteMapping("/{id}")
    public void deleteTrip(@PathVariable Long id) {
        tripService.delete(id);
    }
}
