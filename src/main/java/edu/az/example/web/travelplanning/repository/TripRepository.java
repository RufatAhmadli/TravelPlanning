package edu.az.example.web.travelplanning.repository;

import edu.az.example.web.travelplanning.model.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findAllByDestinationIgnoreCase(String destination);

    @Query("select t from Trip t where t.departureTime = ?1")
    List<Trip> findAllByDepartureTime(LocalDateTime departureTime);

    @Query(value = """
    SELECT t.* FROM trips t
    JOIN users_trips ut ON ut.trip_id = t.id
    WHERE ut.user_id = ?1
    """, nativeQuery = true)
    List<Trip> findAllByUserId(Long userId);

//    @Query("SELECT t FROM Trip t JOIN t.users u WHERE u.id = :userId")
//    List<Trip> findAllByUserId(@Param("userId") Long userId);
}
