package com.example.movieapi.repository;

import com.example.movieapi.entity.MovieEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public class MovieSearchSpecification {
    private MovieSearchSpecification() {
    }

    public static Specification<MovieEntity> search(
            String title,
            String genre,
            Integer year,
            String director,
            Double minRating,
            Double maxRating
    ) {
        Specification<MovieEntity> specification = Specification.unrestricted();

        if (title != null && !title.isBlank()) {
            String normalizedTitle = "%" + title.toLowerCase(Locale.ROOT) + "%";
            specification = specification.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("title")), normalizedTitle));
        }

        if (genre != null && !genre.isBlank()) {
            String normalizedGenre = "%" + genre.toLowerCase(Locale.ROOT) + "%";
            specification = specification.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("genre")), normalizedGenre));
        }

        if (year != null) {
            specification = specification.and((root, query, cb) ->
                    cb.equal(root.get("releaseYear"), year));
        }

        if (director != null && !director.isBlank()) {
            String normalizedDirector = "%" + director.toLowerCase(Locale.ROOT) + "%";
            specification = specification.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("director")), normalizedDirector));
        }

        if (minRating != null) {
            specification = specification.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("rating"), minRating));
        }

        if (maxRating != null) {
            specification = specification.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("rating"), maxRating));
        }

        return specification;
    }
}
