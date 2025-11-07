package edu.az.example.web.travelplanning.repository;

import edu.az.example.web.travelplanning.model.entity.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class TripRepositoryTest {
    @Autowired
    private TripRepository tripRepository;

    private Trip trip;

    @BeforeEach
    void setUp() {
        LocalDateTime customDepartureTime = LocalDateTime.of(2025, 1, 1, 10, 30);
        LocalDateTime customArrivalTime = LocalDateTime.of(2025, 1, 1, 18, 45);

        trip = Trip.builder()
                .arrivalTime(customArrivalTime)
                .departureTime(customDepartureTime)
                .description("Travelling")
                .departure("Baku")
                .destination("New York")
                .price(100.00)
                .build();
        trip.setCreatedAt(LocalDateTime.now());
        trip.setUpdatedAt(LocalDateTime.now());
        trip.setCreatedBy("test-user");
        trip.setUpdatedBy("test-user");
    }

    @Test
    void testFindAllByDestinationIgnoreCase() {
        tripRepository.save(trip);
        List<Trip> res = tripRepository.findAllByDestinationIgnoreCase("New York");

        Trip foundTrip = res.get(0);
        assertEquals("New York", foundTrip.getDestination());
        assertEquals(trip.getDescription(), foundTrip.getDescription());
    }

    @Test
    void testFindAllByDestinationIgnoreCase_NoResults() {
        tripRepository.save(trip);
        List<Trip> res = tripRepository.findAllByDestinationIgnoreCase("London");

        assertTrue(res.isEmpty(), "Expected no trips for destination 'London'");
    }

    @Test
    void testFindAllByDepartureTime_NoResults() {
        tripRepository.save(trip);

        LocalDateTime nonExistingTime = LocalDateTime.of(2030, 1, 1, 12, 0);
        List<Trip> res = tripRepository.findAllByDepartureTime(nonExistingTime);

        assertTrue(res.isEmpty(), "Expected no trips for the given departure time");
    }


    @Test
    void testFindAllByDepartureTime() {
        tripRepository.save(trip);
        List<Trip> res = tripRepository.findAllByDepartureTime(trip.getDepartureTime());

        Trip foundTrip = res.get(0);
        assertEquals("New York", foundTrip.getDestination());
        assertEquals(trip.getDescription(), foundTrip.getDescription());
    }

    @Test
    @Sql(statements = {
            "INSERT INTO users (id, name, family_name, email, password, age, gender,created_at,updated_at,created_by,updated_by) " +
                    "VALUES (1, 'John', 'Doe', 'john@example.com', 'password', 25, 'MALE',CURRENT_TIMESTAMP,current_timestamp,'system','system')",
            "INSERT INTO trips (id, departure, destination, departure_time, arrival_time, description,price,created_at,updated_at,created_by,updated_by) " +
                    "VALUES (1, 'Baku', 'New York', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Travelling',100.00,CURRENT_TIMESTAMP,current_timestamp,'system','system')",
            "INSERT INTO users_trips (user_id, trip_id) VALUES (1, 1)"
    })
    void testFindAllByUserId() {
        List<Trip> result = tripRepository.findAllByUserId(1L);
        Trip foundTrip = result.get(0);

        assertEquals(1, result.size());
        assertEquals("New York", foundTrip.getDestination());
        assertEquals(1, foundTrip.getUsers().get(0).getId());
    }
}