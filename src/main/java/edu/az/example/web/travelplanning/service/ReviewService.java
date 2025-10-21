package edu.az.example.web.travelplanning.service;

import edu.az.example.web.travelplanning.dto.ReviewDto;
import edu.az.example.web.travelplanning.exception.custom.ReviewNotFoundException;
import edu.az.example.web.travelplanning.exception.custom.TripNotFoundException;
import edu.az.example.web.travelplanning.mapper.ReviewMapper;
import edu.az.example.web.travelplanning.model.entity.Trip;
import edu.az.example.web.travelplanning.model.entity.TripReview;
import edu.az.example.web.travelplanning.repository.ReviewRepository;
import edu.az.example.web.travelplanning.repository.TripRepository;
import edu.az.example.web.travelplanning.security.UserSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final UserSecurity userSecurity;
    private final TripRepository tripRepository;

    @Transactional(readOnly = true)
    public ReviewDto getReviewById(Long id) {
        TripReview tripReview = reviewRepository.findById(id)
                .orElseThrow(ReviewNotFoundException::new);

        if (!userSecurity.isOwner(tripReview.getUser().getId())) {
            throw new AccessDeniedException("You are not allowed to get the review");
        }

        return reviewMapper.toReviewDto(tripReview);
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getReviewsByTripId(Long tripId) {
        return reviewRepository.findByTripId(tripId).stream()
                .map(reviewMapper::toReviewDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserId(userId)
                .stream()
                .map(reviewMapper::toReviewDto)
                .toList();
    }

    @Transactional
    public ReviewDto createReview(Long tripId, ReviewDto reviewDto) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException(tripId));

        TripReview reviewEntity = reviewMapper.toReviewEntity(reviewDto);
        reviewEntity.setUser(userSecurity.getCurrentUser());
        reviewEntity.setTrip(trip);

        TripReview saved = reviewRepository.save(reviewEntity);
        return reviewMapper.toReviewDto(saved);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        TripReview review = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);

        if (!userSecurity.isOwner(review.getUser().getId())) {
            throw new AccessDeniedException("You are not allowed to delete this review");
        }

        reviewRepository.deleteById(reviewId);
    }

}
