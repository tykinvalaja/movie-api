package com.example.movieapi.service;

import com.example.movieapi.model.MovieRequestDTO;
import com.example.movieapi.model.MovieResponseDTO;

import java.util.List;
import java.util.Optional;

public interface MoveService {
    List<MovieResponseDTO> getAllMovies();

    Optional<MovieResponseDTO> getMovie(long id);

    MovieResponseDTO addMovie(MovieRequestDTO movie);

    Optional<MovieResponseDTO> updateMovie(long id, MovieRequestDTO movie);

    void deleteMovie(Long id);

    List<MovieResponseDTO> searchMovies(
            String title,
            String genre,
            Integer year,
            String director,
            Double minRating,
            Double maxRating
    );
}
