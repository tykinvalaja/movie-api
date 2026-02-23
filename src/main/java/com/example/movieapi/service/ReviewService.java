package com.example.movieapi.service;

import com.example.movieapi.model.ReviewResponseDTO;
import com.example.movieapi.model.ReviewRequestDTO;

public interface ReviewService {
    ReviewResponseDTO addReview(ReviewRequestDTO reviewDTO, long id);
}
