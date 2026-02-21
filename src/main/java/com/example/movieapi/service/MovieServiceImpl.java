package com.example.movieapi.service;

import com.example.movieapi.model.Movie;
import com.example.movieapi.repository.MovieRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MoveService{

    private final MovieRepository movieRepository;

    @Override
    public List<Movie> getALLMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Movie getMovie(Long id) {
        return movieRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Movie not found with id: " + id));
    }

    @Override
    public Movie addMovie(Movie movie) {
        return null;
    }

    @Override
    public Movie updateMovie(Movie movie) {
        return null;
    }

    @Override
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }
}
