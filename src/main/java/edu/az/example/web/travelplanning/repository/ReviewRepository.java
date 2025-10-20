package edu.az.example.web.travelplanning.repository;

import edu.az.example.web.travelplanning.model.entity.TripReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<TripReview,Long> {

    @Query("select t from TripReview t where t.user.id = ?1")
    List<TripReview> findByUserId(Long userId);

    @Query("select t from TripReview t where t.trip.id = ?1")
    List<TripReview> findByTripId(Long tripId);
}
