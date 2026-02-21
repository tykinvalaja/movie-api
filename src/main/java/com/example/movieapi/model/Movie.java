package com.example.movieapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    String id;
    String title;
    String genre;
    String releaseYear;
    String director;
    double rating;
}
