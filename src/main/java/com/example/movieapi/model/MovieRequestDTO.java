package com.example.movieapi.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieRequestDTO {
    private String title;
    private String genre;
    private int releaseYear;
    private String director;
    private double rating;
}
