package com.example.movieapi.controller;


import com.example.movieapi.model.Movie;
import com.example.movieapi.service.MovieServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieServiceImpl movieService;

    @GetMapping
    public ResponseEntity<List<Movie>> getMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovie(@PathVariable long id) {
        return ResponseEntity.ok(movieService.getMovie(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND)));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Movie>> searchMovie(@RequestParam(required = false) String title,
                                              @RequestParam(required = false) String genre,
                                              @RequestParam(required = false) Integer releaseYear,
                                              @RequestParam(required = false) String director,
                                              @RequestParam(required = false) Double minRating,
                                              @RequestParam(required = false) Double maxRating) {
        return ResponseEntity.ok(movieService.searchMovies(title, genre, releaseYear, director, minRating, maxRating));
    }

    @PostMapping
    public ResponseEntity<Movie> addMovie(Movie movie) {
        return ResponseEntity.status(CREATED).body(movieService.addMovie(movie));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable long id, @RequestBody Movie movie) {
        return ResponseEntity.ok(movieService.updateMovie(id, movie).orElseThrow(() -> new ResponseStatusException(NOT_FOUND)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Movie");
    }
}
