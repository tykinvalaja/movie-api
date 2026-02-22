package com.example.movieapi.service;

import com.example.movieapi.mapper.ReviewMapper;
import com.example.movieapi.entity.MovieEntity;
import com.example.movieapi.entity.ReviewEntity;
import com.example.movieapi.model.ReviewDTO;
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
    public ReviewDTO addReview(ReviewDTO reviewDTO) {
        MovieEntity movie = movieRepository.findById(reviewDTO.getMovieId())
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Movie not found with id " + reviewDTO.getMovieId()));

        ReviewEntity review = reviewMapper.toEntity(reviewDTO);
        review.setMovie(movie);

        return reviewMapper.toDTO(reviewRepository.save(review));
    }
}
