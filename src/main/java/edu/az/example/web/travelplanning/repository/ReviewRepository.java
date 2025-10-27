package edu.az.example.web.travelplanning.repository;

import edu.az.example.web.travelplanning.model.entity.TripReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<TripReview,Long> {

    @Query("select t from TripReview t where t.user.id = ?1")
    List<TripReview> findByUserId(Long userId);

    @Query("select t from TripReview t where t.trip.id = ?1")
    List<TripReview> findByTripId(Long tripId);

    @Query("select (count(t) > 0) from TripReview t where t.user.id = ?1 and t.trip.id = ?2")
    boolean existsByUserIdAndTripId(Long userId, Long tripId);

    @Query("SELECT AVG(r.rating) FROM TripReview r WHERE r.trip.id = :tripId")
    Double calculateAverageRatingByTripId(@Param("tripId") Long tripId);
}
