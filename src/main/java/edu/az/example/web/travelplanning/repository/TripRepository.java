package edu.az.example.web.travelplanning.repository;

import edu.az.example.web.travelplanning.model.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findAllByDestinationIgnoreCase(String destination);

    List<Trip> findAllByDepartureTime(LocalDate departureTime);

    @Query(value = """
    SELECT t.* FROM trips t
    JOIN users_trips ut ON ut.trip_id = t.id
    WHERE ut.user_id = ?1
    """, nativeQuery = true)
    List<Trip> findAllByUserId(Long userId);

//    @Query("SELECT t FROM Trip t JOIN t.users u WHERE u.id = :userId")
//    List<Trip> findAllByUserId(@Param("userId") Long userId);
}
