package com.example.movieapi.controller;


import com.example.movieapi.model.Movie;
import com.example.movieapi.service.MovieServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieServiceImpl movieService;

    @GetMapping
    public ResponseEntity<List<Movie>> getMovies() {
        return ResponseEntity.ok(movieService.getALLMovies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getMovie(@PathVariable String id) {
        return ResponseEntity.ok("Movie");
    }

    @GetMapping("/search")
    public ResponseEntity<String> searchMovie(@RequestParam  String title,
                                              @RequestParam(required = false) String genre,
                                              @RequestParam(required = false) String releaseYear,
                                              @RequestParam(required = false) String director,
                                              @RequestParam(required = false) String rating) {
        return ResponseEntity.ok("Movie");
    }

    @PostMapping
    public ResponseEntity<String> addMovie() {
        return ResponseEntity.ok("Movie");
    }

    @PutMapping
    public ResponseEntity<String> updateMovie() {
        return ResponseEntity.ok("Movie");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteMovie() {
        return ResponseEntity.ok("Movie");
    }
}
