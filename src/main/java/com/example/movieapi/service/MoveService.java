package com.example.movieapi.service;

import com.example.movieapi.model.Movie;

import java.util.List;

public interface MoveService {
    List<Movie> getALLMovies();
    Movie getMovie(Long id);
    Movie addMovie(Movie movie);
    Movie updateMovie(Movie movie);
    void deleteMovie(Long id);

    // TODO: SEARCH
}
