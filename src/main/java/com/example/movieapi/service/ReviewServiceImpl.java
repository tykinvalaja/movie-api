package com.example.movieapi.service;

import com.example.movieapi.mapper.ReviewMapper;
import com.example.movieapi.entity.MovieEntity;
import com.example.movieapi.entity.ReviewEntity;
import com.example.movieapi.model.ReviewResponseDTO;
import com.example.movieapi.model.ReviewRequestDTO;
import com.example.movieapi.repository.MovieRepository;
import com.example.movieapi.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public ReviewResponseDTO addReview(ReviewRequestDTO reviewDTO, long id) {
        MovieEntity movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Movie not found with id " + id));

        ReviewEntity review = reviewMapper.toEntity(reviewDTO);
        review.setMovie(movie);

        return reviewMapper.toDTO(reviewRepository.save(review));
    }
}
