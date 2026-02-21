package com.example.movieapi.service;

import com.example.movieapi.model.Movie;

import java.util.List;
import java.util.Optional;

public interface MoveService {
    List<Movie> getAllMovies();

    Optional<Movie> getMovie(long id);

    Movie addMovie(Movie movie);

    Optional<Movie> updateMovie(long id, Movie movie);

    void deleteMovie(Long id);

    List<Movie> searchMovies(
            String title,
            String genre,
            Integer year,
            String director,
            Double minRating,
            Double maxRating
    );
}
