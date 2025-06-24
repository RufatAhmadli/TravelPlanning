package edu.az.example.web.travelplanning.service;

import edu.az.example.web.travelplanning.mapper.TripMapper;
import edu.az.example.web.travelplanning.model.dto.TripDto;
import edu.az.example.web.travelplanning.model.entity.Trip;
import edu.az.example.web.travelplanning.repository.TripRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripService {
    private final TripRepository tripRepository;
    private final TripMapper tripMapper;

    public List<TripDto> findAll() {
        return tripRepository.findAll()
                .stream()
                .map(tripMapper::toTripDto)
                .toList();
    }

    public TripDto findById(Long id) {
        return tripRepository.findById(id)
                .map(tripMapper::toTripDto)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<TripDto> findAllByDestination(String destination) {
        return tripRepository.findAllByDestinationIgnoreCase(destination)
                .stream()
                .map(tripMapper::toTripDto)
                .toList();
    }

    public List<TripDto> findAllByDepartureTime(LocalDate departureTime) {
        return tripRepository.findAllByDepartureTime(departureTime)
                .stream()
                .map(tripMapper::toTripDto)
                .toList();
    }

    public List<TripDto> findAllByUserId(Long userId) {
        return tripRepository.findAllByUserId(userId)
                .stream()
                .map(tripMapper::toTripDto)
                .toList();
    }

    public TripDto create(TripDto tripDto) {
        if (tripDto == null) {
            throw new IllegalArgumentException();
        }
        Trip trip = tripMapper.toTrip(tripDto);
        tripRepository.save(trip);
        return tripMapper.toTripDto(trip);
    }

    public TripDto update(Long id, TripDto tripDto) {
        if (tripDto == null || id == null) {
            throw new IllegalArgumentException();
        }
        Trip trip = tripRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        tripMapper.toTripEntity(trip, tripDto);
        return tripMapper.toTripDto(tripRepository.save(trip));
    }

    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        if (!tripRepository.existsById(id)) {
            throw new EntityNotFoundException();
        }
        tripRepository.deleteById(id);
    }
}
