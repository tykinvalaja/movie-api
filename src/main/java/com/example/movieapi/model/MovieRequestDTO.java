package com.example.movieapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieRequestDTO {
    @Schema(description = "Movie title", example = "The Matrix")
    private String title;
    @Schema(description = "Movie genre", example = "Science Fiction")
    private String genre;
    @Schema(description = "Release year", example = "1999")
    private int releaseYear;
    @Schema(description = "Director name", example = "Lana Wachowski")
    private String director;
    @Schema(description = "Movie rating from 0 to 10", example = "8.7")
    private double rating;
}
