package edu.az.example.web.travelplanning.service;

import edu.az.example.web.travelplanning.mapper.TripMapper;
import edu.az.example.web.travelplanning.model.dto.TripDto;
import edu.az.example.web.travelplanning.model.entity.Address;
import edu.az.example.web.travelplanning.model.entity.Trip;
import edu.az.example.web.travelplanning.repository.TripRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {
    @Mock
    private TripRepository tripRepository;
    @Mock
    private TripMapper tripMapper;
    @InjectMocks
    private TripService tripService;

    private Trip trip;
    private TripDto tripDto;
    private TripDto updatedDto;

    @BeforeEach
    void setUp() {
        trip = Trip.builder()
                .id(1L)
                .arrivalTime(LocalDate.now())
                .departureTime(LocalDate.now())
                .description("Travelling")
                .departure("Baku")
                .destination("New York")
                .build();
        tripDto = TripDto.builder()
                .id(1L)
                .arrivalTime(LocalDate.now())
                .departureTime(LocalDate.now())
                .description("Travelling")
                .departure("Baku")
                .destination("New York")
                .build();
        updatedDto = TripDto.builder()
                .id(1L)
                .arrivalTime(LocalDate.now())
                .departureTime(LocalDate.now())
                .description("Travelling")
                .departure("Paris")
                .destination("London")
                .build();
    }

    @Test
    void testFindAll() {
        when(tripRepository.findAll()).thenReturn(List.of(trip));
        when(tripMapper.toTripDto(trip)).thenReturn(tripDto);

        List<TripDto> res = tripService.findAll();
        TripDto foundDto = res.get(0);
        assertEquals(trip.getId(), foundDto.getId());
        assertEquals(trip.getArrivalTime(), foundDto.getArrivalTime());
        assertEquals(trip.getDestination(), foundDto.getDestination());
        assertEquals(trip.getDeparture(), foundDto.getDeparture());
    }

    @Test
    void testFindById() {
        when(tripRepository.findById(1L)).thenReturn(java.util.Optional.of(trip));
        when(tripMapper.toTripDto(trip)).thenReturn(tripDto);

        TripDto foundDto = tripService.findById(1L);
        assertNotNull(foundDto);
        assertEquals(trip.getId(), foundDto.getId());
        assertEquals(trip.getArrivalTime(), foundDto.getArrivalTime());
        assertEquals(trip.getDestination(), foundDto.getDestination());
        assertEquals(trip.getDeparture(), foundDto.getDeparture());
    }

    @Test
    void testFindAllByDestination() {
        when(tripRepository.findAllByDestinationIgnoreCase("New York")).
                thenReturn(List.of(trip));
        when(tripMapper.toTripDto(trip)).thenReturn(tripDto);

        List<TripDto> res = tripService.findAllByDestination("New York");
        assertFalse(res.isEmpty());
        TripDto foundDto = res.get(0);
        assertEquals(trip.getId(), foundDto.getId());
        assertEquals(trip.getArrivalTime(), foundDto.getArrivalTime());
        assertEquals(trip.getDestination(), foundDto.getDestination());
        assertEquals(trip.getDeparture(), foundDto.getDeparture());
    }

    @Test
    void testFindAllByDepartureTime() {
        when(tripRepository.findAllByDepartureTime(LocalDate.now())).
                thenReturn(List.of(trip));
        when(tripMapper.toTripDto(trip)).thenReturn(tripDto);

        List<TripDto> res = tripService.findAllByDepartureTime(LocalDate.now());
        TripDto foundDto = res.get(0);
        assertEquals(trip.getId(), foundDto.getId());
        assertEquals(trip.getArrivalTime(), foundDto.getArrivalTime());
        assertEquals(trip.getDestination(), foundDto.getDestination());
        assertEquals(trip.getDeparture(), foundDto.getDeparture());
    }

    @Test
    void findAllByUserId() {
        when(tripRepository.findAllByUserId(1L)).thenReturn(List.of(trip));
        when(tripMapper.toTripDto(trip)).thenReturn(tripDto);

        List<TripDto> res = tripService.findAllByUserId(1L);
        TripDto foundDto = res.get(0);
        assertEquals(trip.getId(), foundDto.getId());
        assertEquals(trip.getArrivalTime(), foundDto.getArrivalTime());
        assertEquals(trip.getDestination(), foundDto.getDestination());
        assertEquals(trip.getDeparture(), foundDto.getDeparture());
    }

    @Test
    void testCreate() {
        when(tripRepository.save(trip)).thenReturn(trip);
        when(tripMapper.toTripDto(trip)).thenReturn(tripDto);
        when(tripMapper.toTrip(tripDto)).thenReturn(trip);

        TripDto res = tripService.create(tripDto);
        ArgumentCaptor<Trip> captor = ArgumentCaptor.forClass(Trip.class);
        verify(tripRepository).save(captor.capture());
        Trip foundTrip = captor.getValue();
        assertEquals(foundTrip.getId(), res.getId());
        assertEquals(foundTrip.getArrivalTime(), res.getArrivalTime());
        assertEquals(foundTrip.getDestination(), res.getDestination());
        assertEquals(foundTrip.getDeparture(), res.getDeparture());

        verify(tripRepository, times(1)).save(any(Trip.class));
    }

    @Test
    void testCreateNullDto() {
        assertThrows(IllegalArgumentException.class, () -> tripService.create(null));
        verify(tripRepository, never()).save(any(Trip.class));
    }

    @Test
    void testUpdate() {
        when(tripRepository.findById(1L)).thenReturn(java.util.Optional.of(trip));
        doNothing().when(tripMapper).toTripEntity(trip, tripDto);
        when(tripMapper.toTripDto(trip)).thenReturn(updatedDto);
        when(tripRepository.save(trip)).thenReturn(trip);

        TripDto res = tripService.update(1L, tripDto);
        assertEquals(trip.getId(), res.getId());
        assertEquals(trip.getArrivalTime(), res.getArrivalTime());
        assertEquals("London", res.getDestination());
        assertEquals("Paris", res.getDeparture());
    }

    @Test
    void testUpdateNullId() {
        assertThrows(IllegalArgumentException.class, () -> tripService.update(null, tripDto));
        verifyNoInteractions(tripRepository);

    }

    @Test
    void testUpdateNullDto() {
        assertThrows(IllegalArgumentException.class, () -> tripService.update(1L, null));
        verifyNoInteractions(tripRepository);
    }

    @Test
    void testUpdateNonExistentDto() {
        when(tripRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> tripService.update(1L, tripDto));
        verify(tripRepository, never()).save(any(Trip.class));
    }

    @Test
    void testDelete() {
        when(tripRepository.existsById(999L)).thenReturn(true);
        tripService.delete(999L);
        verify(tripRepository).deleteById(999L);
    }

    @Test
    @DisplayName("Should throw exception when id is null")
    void testDeleteNullId() {
        assertThrows(IllegalArgumentException.class, () -> tripService.delete(null));
    }

    @Test
    @DisplayName("Should throw exception when trip is not found")
    void testDeleteNotFoundAddress() {
        when(tripRepository.existsById(999L)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> tripService.delete(999L));
    }
}