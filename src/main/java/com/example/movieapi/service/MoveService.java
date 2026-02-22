package com.example.movieapi.service;

import com.example.movieapi.model.MovieRequestDTO;
import com.example.movieapi.model.MovieResponseDTO;
import com.example.movieapi.model.MovieReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MoveService {
    Page<MovieResponseDTO> getAllMovies(Pageable pageable);

    Optional<MovieReviewDTO> getMovie(long id);

    MovieResponseDTO addMovie(MovieRequestDTO movie);

    Optional<MovieResponseDTO> updateMovie(long id, MovieRequestDTO movie);

    void deleteMovie(Long id);

    Page<MovieResponseDTO> searchMovies(
            String title,
            String genre,
            Integer year,
            String director,
            Double minRating,
            Double maxRating,
            Pageable pageable
    );
}
