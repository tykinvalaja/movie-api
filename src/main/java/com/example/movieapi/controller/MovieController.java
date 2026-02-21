package com.example.movieapi.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movie")
public class MovieController {

    @GetMapping
    public ResponseEntity<String> getMovies() {
        return ResponseEntity.ok("Movies");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getMovie(@PathVariable String id) {
        return ResponseEntity.ok("Movie");
    }

    @GetMapping("/search")
    public ResponseEntity<String> searchMovie(@RequestParam String title, @RequestParam String genre, @RequestParam String releaseYear,
                                              @RequestParam String director, @RequestParam String rating) {
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
