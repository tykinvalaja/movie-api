package com.example.movieapi.controller;

import com.example.movieapi.model.ReviewDTO;
import com.example.movieapi.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Movie review endpoints")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    @Operation(
            summary = "Add a review",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Review added"),
                    @ApiResponse(responseCode = "404", description = "Movie not found")
            }
    )
    public ResponseEntity<ReviewDTO> addReview(@RequestBody ReviewDTO reviewDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.addReview(reviewDTO));
    }
}
