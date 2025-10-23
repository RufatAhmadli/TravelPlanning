package edu.az.example.web.travelplanning.controller;

import edu.az.example.web.travelplanning.dto.ReviewDto;
import edu.az.example.web.travelplanning.service.ReviewService;
import edu.az.example.web.travelplanning.validation.OnCreate;
import edu.az.example.web.travelplanning.validation.OnUpdate;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDto>> getAllReviewsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(reviewService.getReviewsByUserId(userId));
    }

    @GetMapping("/trip/{tripId}")
    public ResponseEntity<List<ReviewDto>> getAllReviewsByTripId(@PathVariable Long tripId) {
        return ResponseEntity.ok(reviewService.getReviewsByTripId(tripId));
    }

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@RequestParam Long tripId,
                                                  @Validated({OnCreate.class, Default.class})
                                                  @RequestBody ReviewDto reviewDto) {
        return new ResponseEntity<>
                (reviewService.createReview(tripId, reviewDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable Long id,
                                                  @Validated({OnUpdate.class, Default.class})
                                                  @RequestBody ReviewDto reviewDto) {
        return new ResponseEntity<>(reviewService.updateReview(id, reviewDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReviewById(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }
}
