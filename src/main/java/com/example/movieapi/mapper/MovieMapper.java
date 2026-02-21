package com.example.movieapi.mapper;

import com.example.movieapi.entity.MovieEntity;
import com.example.movieapi.model.Movie;

public class MovieMapper {

    public static Movie toDto(final MovieEntity movieEntity) {
        return Movie.builder()
                .id(movieEntity.getId())
                .title(movieEntity.getTitle())
                .genre(movieEntity.getGenre())
                .releaseYear(movieEntity.getReleaseYear())
                .director(movieEntity.getDirector())
                .rating(movieEntity.getRating())
                .build();
    }

    public static MovieEntity toEntity(final Movie movie) {
        return MovieEntity.builder()
                .title(movie.getTitle())
                .genre(movie.getGenre())
                .releaseYear(movie.getReleaseYear())
                .director(movie.getDirector())
                .rating(movie.getRating())
                .build();
    }
}
