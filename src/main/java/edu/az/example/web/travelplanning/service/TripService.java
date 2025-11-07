package edu.az.example.web.travelplanning.service;

import edu.az.example.web.travelplanning.exception.custom.TripNotFoundException;
import edu.az.example.web.travelplanning.exception.custom.UserNotFoundException;
import edu.az.example.web.travelplanning.mapper.TripMapper;
import edu.az.example.web.travelplanning.dto.TripDto;
import edu.az.example.web.travelplanning.model.entity.Trip;
import edu.az.example.web.travelplanning.model.entity.User;
import edu.az.example.web.travelplanning.repository.TripRepository;
import edu.az.example.web.travelplanning.repository.UserRepository;
import edu.az.example.web.travelplanning.security.UserSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripService {
    private final TripRepository tripRepository;
    private final TripMapper tripMapper;
    private final UserSecurity userSecurity;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<TripDto> findAll() {
        return tripRepository.findAll()
                .stream()
                .map(tripMapper::toTripDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public TripDto findById(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new TripNotFoundException(id));
        if (isUserNotInTrip(trip)) {
            throw new SecurityException("Cannot get trip you don't have access to");
        }
        return tripMapper.toTripDto(trip);
    }

    @Transactional(readOnly = true)
    public List<TripDto> findAllByDestination(String destination) {
        return tripRepository.findAllByDestinationIgnoreCase(destination)
                .stream()
                .map(tripMapper::toTripDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TripDto> findAllByDepartureTime(LocalDateTime departureTime) {
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

    @Transactional
    public TripDto create(TripDto tripDto) {
        if (tripDto == null) {
            throw new IllegalArgumentException();
        }

        User currentUser = userSecurity.getCurrentUser();
        Trip trip = tripMapper.toTripEntity(tripDto);
        Trip savedTrip = tripRepository.save(trip);
        currentUser.addTrip(savedTrip);
        return tripMapper.toTripDto(trip);
    }

    @Transactional
    public TripDto addTripToUser(Long tripId, Long userId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException(tripId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        user.addTrip(trip);
        tripRepository.save(trip);
        return tripMapper.toTripDto(trip);
    }

    @Transactional
    public TripDto update(Long id, TripDto tripDto) {
        if (tripDto == null || id == null) {
            throw new IllegalArgumentException();
        }
        Trip trip = tripRepository.findById(id).
                orElseThrow(() -> new TripNotFoundException(id));

        if (isUserNotInTrip(trip)) {
            throw new SecurityException("Cannot update trip you don't have access to");
        }

        tripMapper.updateTripEntity(trip, tripDto);
        return tripMapper.toTripDto(tripRepository.save(trip));
    }

    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        Trip trip = tripRepository.findById(id)
                .orElseThrow(() ->
                        new TripNotFoundException(id));

        if (isUserNotInTrip(trip)) {
            throw new SecurityException("Cannot delete trip you don't have access to");
        }

        // to avoid ConcurrentModificationException
        List<User> usersCopy = new ArrayList<>(trip.getUsers());
        for (User user : usersCopy) {
            user.removeTrip(trip);
        }
        tripRepository.deleteById(id);
    }

    private boolean isUserNotInTrip(Trip trip) {
        User currentUser = userSecurity.getCurrentUser();
        return !trip.getUsers().contains(currentUser);
    }
}
