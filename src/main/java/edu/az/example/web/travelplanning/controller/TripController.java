package edu.az.example.web.travelplanning.controller;

import edu.az.example.web.travelplanning.model.dto.TripDto;
import edu.az.example.web.travelplanning.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/trips")
public class TripController {
    private final TripService tripService;

    @GetMapping
    public List<TripDto> getAll() {
        return tripService.findAll();
    }

    @GetMapping("/{id}")
    public TripDto getById(@PathVariable Long id) {
        return tripService.findById(id);
    }

    @GetMapping("/destination/{destination}")
    public List<TripDto> getByDestination(@PathVariable String destination) {
        return tripService.findAllByDestination(destination);
    }

    @GetMapping("/time/{departureTime}")
    public List<TripDto> getByDepartureTime(@PathVariable LocalDate departureTime) {
        return tripService.findAllByDepartureTime(departureTime);
    }

    @GetMapping("users/{userId}")
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
