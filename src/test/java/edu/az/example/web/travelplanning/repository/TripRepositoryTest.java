package edu.az.example.web.travelplanning.repository;

import edu.az.example.web.travelplanning.model.entity.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TripRepositoryTest {
    @Autowired
    private TripRepository tripRepository;

    private Trip trip;

    @BeforeEach
    void setUp() {
        trip = Trip.builder()
                .arrivalTime(LocalDate.now())
                .departureTime(LocalDate.now())
                .description("Travelling")
                .departure("Baku")
                .destination("New York")
                .build();
    }
    @Test
    void testFindAllByDestinationIgnoreCase() {
        tripRepository.save(trip);
        List<Trip> res = tripRepository.findAllByDestinationIgnoreCase("New York");

        Trip foundTrip = res.get(0);
        assertEquals("New York", foundTrip.getDestination());
        assertEquals(LocalDate.now(), foundTrip.getArrivalTime());
        assertEquals(trip.getDescription(), foundTrip.getDescription());
    }

    @Test
    void testFindAllByDepartureTime() {
        tripRepository.save(trip);
        List<Trip> res = tripRepository.findAllByDepartureTime(LocalDate.now());

        Trip foundTrip = res.get(0);
        assertEquals("New York", foundTrip.getDestination());
        assertEquals(LocalDate.now(), foundTrip.getArrivalTime());
        assertEquals(trip.getDescription(), foundTrip.getDescription());
    }

    @Test
    @Sql(statements = {
            "INSERT INTO users (id, name, family_name, email, password, age, gender) VALUES (1, 'John', 'Doe', 'john@example.com', 'password', 25, 'MALE')",
            "INSERT INTO trips (id, departure, destination, departure_time, arrival_time, description) VALUES (1, 'Baku', 'New York', CURRENT_DATE, CURRENT_DATE, 'Travelling')",
            "INSERT INTO users_trips (user_id, trip_id) VALUES (1, 1)"
    })
    void testFindAllByUserId() {
        List<Trip> result = tripRepository.findAllByUserId(1L);
        Trip foundTrip = result.get(0);

        assertEquals(1, result.size());
        assertEquals("New York", foundTrip.getDestination());
        assertEquals(LocalDate.now(), foundTrip.getArrivalTime());
        assertEquals(1,foundTrip.getUsers().get(0).getId());
    }
}