package com.example.movieapi.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.example.movieapi.model.MovieRequestDTO;
import com.example.movieapi.model.MovieResponseDTO;
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
@Tag(name = "Movies", description = "Movie catalog endpoints")
public class MovieController {
    private final MovieServiceImpl movieService;

    @GetMapping
    @Operation(
            summary = "Get all movies",
            responses = @ApiResponse(responseCode = "200", description = "Movies returned")
    )
    public ResponseEntity<List<MovieResponseDTO>> getMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get movie by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Movie found"),
                    @ApiResponse(responseCode = "404", description = "Movie not found")
            }
    )
    public ResponseEntity<MovieResponseDTO> getMovie(
            @Parameter(description = "Movie identifier", example = "1")
            @PathVariable long id) {
        return ResponseEntity.ok(movieService.getMovie(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND)));
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search movies",
            description = "Filter by any combination of title, genre, release year, director and rating range",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Matching movies returned",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = MovieResponseDTO.class)))
            )
    )
    public ResponseEntity<List<MovieResponseDTO>> searchMovie(
            @Parameter(description = "Title contains (case-insensitive)", example = "matrix")
            @RequestParam(required = false) String title,
            @Parameter(description = "Genre contains (case-insensitive)", example = "sci-fi")
            @RequestParam(required = false) String genre,
            @Parameter(description = "Exact release year", example = "1999")
            @RequestParam(required = false) Integer releaseYear,
            @Parameter(description = "Director contains (case-insensitive)", example = "wachowski")
            @RequestParam(required = false) String director,
            @Parameter(description = "Minimum rating inclusive", example = "7.5")
            @RequestParam(required = false) Double minRating,
            @Parameter(description = "Maximum rating inclusive", example = "9.0")
            @RequestParam(required = false) Double maxRating) {
        return ResponseEntity.ok(movieService.searchMovies(title, genre, releaseYear, director, minRating, maxRating));
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
            @Parameter(description = "Movie identifier", example = "1")
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
            @Parameter(description = "Movie identifier", example = "1")
            @PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Movie");
    }
}
