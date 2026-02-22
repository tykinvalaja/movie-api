package com.example.movieapi.controller;


import com.example.movieapi.model.MovieReviewDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.example.movieapi.model.MovieRequestDTO;
import com.example.movieapi.model.MovieResponseDTO;
import com.example.movieapi.service.MovieServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
@Tag(name = "Movies", description = "Movie catalog endpoints")
public class MovieController {
    private final MovieServiceImpl movieService;

    @GetMapping
    @Operation(
            summary = "Get all movies",
            responses = @ApiResponse(responseCode = "200", description = "Movies returned")
    )
    public ResponseEntity<PagedModel<MovieResponseDTO>> getMovies(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size,
                                                                  @RequestParam(defaultValue = "title") String sortBy,
                                                                  @RequestParam(defaultValue = "asc") String direction) {
        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(movieService.getAllMovies(pageable));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get movie by ID, reviews included",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Movie found"),
                    @ApiResponse(responseCode = "404", description = "Movie not found")
            }
    )
    public ResponseEntity<MovieReviewDTO> getMovie(
            @PathVariable long id) {
        return ResponseEntity.ok(movieService.getMovie(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND)));
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search movies",
            description = "Filter by any combination of title, genre, release year, director and rating range",
            responses = @ApiResponse(responseCode = "200", description = "Matching movies returned")
    )
    public ResponseEntity<PagedModel<MovieResponseDTO>> searchMovie(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer releaseYear,
            @RequestParam(required = false) String director,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxRating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(movieService.searchMovies(title, genre, releaseYear, director, minRating, maxRating, pageable));
    }

    @PostMapping
    @Operation(
            summary = "Add a movie",
            responses = @ApiResponse(responseCode = "201", description = "Movie added")
    )
    public ResponseEntity<MovieResponseDTO> addMovie(
            @RequestBody MovieRequestDTO movie) {
        return ResponseEntity.status(CREATED).body(movieService.addMovie(movie));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a movie",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Movie updated"),
                    @ApiResponse(responseCode = "404", description = "Movie not found")
            }
    )
    public ResponseEntity<MovieResponseDTO> updateMovie(
            @PathVariable long id,
            @RequestBody MovieRequestDTO movie) {
        return ResponseEntity.ok(movieService.updateMovie(id, movie).orElseThrow(() -> new ResponseStatusException(NOT_FOUND)));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a movie",
            responses = @ApiResponse(responseCode = "204", description = "Movie deleted")
    )
    public ResponseEntity<String> deleteMovie(
            @PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Movie");
    }
}
