package edu.az.example.web.travelplanning.service;
import edu.az.example.web.travelplanning.dto.TripDto;
import edu.az.example.web.travelplanning.exception.custom.TripNotFoundException;
import edu.az.example.web.travelplanning.mapper.TripMapper;
import edu.az.example.web.travelplanning.model.entity.Trip;
import edu.az.example.web.travelplanning.model.entity.User;
import edu.az.example.web.travelplanning.repository.TripRepository;
import edu.az.example.web.travelplanning.repository.UserRepository;
import edu.az.example.web.travelplanning.security.UserSecurity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @Mock
    private TripRepository tripRepository;
    @Mock
    private TripMapper tripMapper;
    @Mock
    private UserSecurity userSecurity;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private TripService tripService;

    private Trip trip;
    private TripDto tripDto;
    private TripDto updatedDto;
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("John")
                .lastName("Doe")
                .email("john@example.com")
                .password("12345")
                .trips(new ArrayList<>())
                .build();

        trip = Trip.builder()
                .id(1L)
                .arrivalTime(LocalDateTime.now())
                .departureTime(LocalDateTime.now())
                .description("Vacation trip")
                .departure("Baku")
                .destination("New York")
                .users(new ArrayList<>(List.of(user)))
                .build();

        tripDto = TripDto.builder()
                .id(1L)
                .arrivalTime(LocalDate.now())
                .departureTime(LocalDate.now())
                .description("Vacation trip")
                .departure("Baku")
                .destination("New York")
                .build();

        updatedDto = TripDto.builder()
                .id(1L)
                .arrivalTime(LocalDate.now())
                .departureTime(LocalDate.now())
                .description("Updated trip")
                .departure("Paris")
                .destination("London")
                .build();
    }

    @Test
    void testFindAll() {
        when(tripRepository.findAll()).thenReturn(List.of(trip));
        when(tripMapper.toTripDto(trip)).thenReturn(tripDto);

        List<TripDto> res = tripService.findAll();
        assertEquals(1, res.size());
        TripDto found = res.get(0);
        assertEquals(trip.getId(), found.getId());
        assertEquals(trip.getDestination(), found.getDestination());
    }

    @Test
    void testFindById_whenUserHasAccess() {
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(userSecurity.getCurrentUser()).thenReturn(user);
        when(tripMapper.toTripDto(trip)).thenReturn(tripDto);

        TripDto result = tripService.findById(1L);
        assertEquals(trip.getId(), result.getId());
        assertEquals(trip.getDestination(), result.getDestination());
    }

    @Test
    void testFindById_whenUserNotInTrip() {
        User anotherUser = User.builder().id(99L).build();
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(userSecurity.getCurrentUser()).thenReturn(anotherUser);

        assertThrows(SecurityException.class, () -> tripService.findById(1L));
    }

    @Test
    void testFindById_notFound() {
        when(tripRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(TripNotFoundException.class, () -> tripService.findById(999L));
    }

    @Test
    void testCreate() {
        when(userSecurity.getCurrentUser()).thenReturn(user);
        when(tripMapper.toTripEntity(any(TripDto.class))).thenReturn(trip);
        when(tripRepository.save(any(Trip.class))).thenReturn(trip);
        when(tripMapper.toTripDto(any(Trip.class))).thenReturn(tripDto);

        TripDto result = tripService.create(tripDto);

        ArgumentCaptor<Trip> captor = ArgumentCaptor.forClass(Trip.class);
        verify(tripRepository).save(captor.capture());
        Trip savedTrip = captor.getValue();

        assertEquals(trip.getDeparture(), result.getDeparture());
        assertEquals(trip.getDestination(), result.getDestination());
        assertTrue(user.getTrips().contains(savedTrip));
        verify(tripRepository, times(1)).save(any(Trip.class));
    }

    @Test
    void testCreateNullDto() {
        assertThrows(IllegalArgumentException.class, () -> tripService.create(null));
        verify(tripRepository, never()).save(any());
    }

    @Test
    void testUpdate_whenUserHasAccess() {
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(userSecurity.getCurrentUser()).thenReturn(user);
        doNothing().when(tripMapper).updateTripEntity(trip, tripDto);
        when(tripMapper.toTripDto(trip)).thenReturn(updatedDto);
        when(tripRepository.save(trip)).thenReturn(trip);

        TripDto result = tripService.update(1L, tripDto);
        assertEquals("London", result.getDestination());
        assertEquals("Paris", result.getDeparture());
    }

    @Test
    void testUpdate_whenUserHasNoAccess() {
        User anotherUser = User.builder().id(2L).build();
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(userSecurity.getCurrentUser()).thenReturn(anotherUser);

        assertThrows(SecurityException.class, () -> tripService.update(1L, tripDto));
    }

    @Test
    void testUpdate_notFound() {
        when(tripRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(TripNotFoundException.class, () -> tripService.update(1L, tripDto));
    }

    @Test
    void testUpdateNullArguments() {
        assertThrows(IllegalArgumentException.class, () -> tripService.update(1L, null));
        assertThrows(IllegalArgumentException.class, () -> tripService.update(null, tripDto));
    }

    @Test
    void testDelete_whenUserHasAccess() {
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(userSecurity.getCurrentUser()).thenReturn(user);

        tripService.delete(1L);
        verify(tripRepository).deleteById(1L);
    }

    @Test
    void testDelete_whenUserHasNoAccess() {
        User anotherUser = User.builder().id(2L).build();
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(userSecurity.getCurrentUser()).thenReturn(anotherUser);

        assertThrows(SecurityException.class, () -> tripService.delete(1L));
        verify(tripRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDelete_notFound() {
        when(tripRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(TripNotFoundException.class, () -> tripService.delete(999L));
    }

    @Test
    void testDeleteNullId() {
        assertThrows(IllegalArgumentException.class, () -> tripService.delete(null));
    }
}