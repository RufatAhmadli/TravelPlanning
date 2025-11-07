package edu.az.example.web.travelplanning.repository;

import edu.az.example.web.travelplanning.TestAuditable;
import edu.az.example.web.travelplanning.model.entity.Trip;
import edu.az.example.web.travelplanning.model.entity.TripReview;
import edu.az.example.web.travelplanning.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static edu.az.example.web.travelplanning.enums.Gender.MALE;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ReviewRepositoryTest implements TestAuditable {
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    private TripReview review;
    
    @BeforeEach
    void setUp() {
        LocalDateTime customDepartureTime = LocalDateTime.of(2025, 1, 1, 10, 30);
        LocalDateTime customArrivalTime = LocalDateTime.of(2025, 1, 1, 18, 45);
        
        User user = User.builder()
                .name("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .age(13)
                .gender(MALE)
                .build();
        assignAuditFields(user);
        entityManager.persistAndFlush(user);

        Trip trip = Trip.builder()
                .arrivalTime(customArrivalTime)
                .departureTime(customDepartureTime)
                .description("Travelling")
                .departure("Baku")
                .destination("New York")
                .price(100.00)
                .build();
        assignAuditFields(trip);
        entityManager.persistAndFlush(trip);
        
        review = TripReview.builder()
                .rating(4)
                .comment("Amazing")
                .user(user)
                .trip(trip)
                .build();
        assignAuditFields(review);

    }

    @Test
    void findByUserId() {
        entityManager.persistAndFlush(review);
        entityManager.clear();

        List<TripReview> res = reviewRepository.findByUserId(review.getUser().getId());
        assertFalse(res.isEmpty());

        TripReview found = res.get(0);
        assertEquals(review.getUser().getId(), found.getUser().getId());
        assertEquals(review.getTrip().getId(), found.getTrip().getId());
        assertEquals(review.getRating(), found.getRating());
    }

    @Test
    void findByTripId() {
        entityManager.persistAndFlush(review);
        entityManager.clear();

        List<TripReview> res = reviewRepository.findByTripId(review.getTrip().getId());
        assertFalse(res.isEmpty());

        TripReview found = res.get(0);
        assertEquals(review.getUser().getId(), found.getUser().getId());
        assertEquals(review.getTrip().getId(), found.getTrip().getId());
        assertEquals(review.getRating(), found.getRating());
    }

    @Test
    void existsByUserIdAndTripId() {
        entityManager.persistAndFlush(review);
        entityManager.clear();

        boolean doesExist = reviewRepository.existsByUserIdAndTripId(
                review.getUser().getId(), review.getTrip().getId());
        assertTrue(doesExist);
    }

    @Test
    void doesNotExistByUserIdAndTripId() {
        entityManager.persistAndFlush(review);
        entityManager.clear();

        boolean doesExist = reviewRepository.existsByUserIdAndTripId(1L,2L);
        assertFalse(doesExist);
    }

    @Test
    void calculateAverageRatingByTripId() {
        entityManager.persistAndFlush(review);

        User user2 = User.builder()
                .name("James")
                .lastName("Maddison")
                .email("james123@example.com")
                .password("frwerew12!")
                .age(21)
                .gender(MALE)
                .build();
        assignAuditFields(user2);
        entityManager.persistAndFlush(user2);

        TripReview anotherReview = TripReview.builder()
                .rating(2)
                .comment("Not great")
                .user(user2)
                .trip(review.getTrip())
                .build();
        assignAuditFields(anotherReview);
        entityManager.persistAndFlush(anotherReview);
        entityManager.clear();

        Double avg = reviewRepository.calculateAverageRatingByTripId(review.getTrip().getId());
        assertEquals(3.0, avg);
    }

    @Test
    void calculateAverageRatingByTripId_shouldReturnNullWhenNoReviewsExist() {
        TripReview anotherReview = TripReview.builder()
                .rating(5)
                .comment("Excellent")
                .user(review.getUser())
                .trip(review.getTrip())
                .build();
        assignAuditFields(anotherReview);

        Double avg = reviewRepository.calculateAverageRatingByTripId(999L);
        assertNull(avg, "Average rating should be null when there are no reviews for the trip");

    }
}