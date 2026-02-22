package com.example.movieapi.service;

import com.example.movieapi.entity.MovieEntity;
import com.example.movieapi.mapper.MovieMapper;
import com.example.movieapi.mapper.ReviewMapper;
import com.example.movieapi.model.MovieRequestDTO;
import com.example.movieapi.model.MovieResponseDTO;
import com.example.movieapi.model.MovieReviewDTO;
import com.example.movieapi.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final ReviewMapper reviewMapper;

    @Override
    public PagedModel<MovieResponseDTO> getAllMovies(Pageable pageable) {
        return new PagedModel<>(movieRepository.findAll(pageable).map(movieMapper::toResponseDTO));
    }

    @Override
    public Optional<MovieReviewDTO> getMovie(long id) {
        Optional<MovieEntity> entity = movieRepository.findById(id);
        MovieReviewDTO movieReviewDTO = new MovieReviewDTO();

        if (entity.isPresent()) {
            movieReviewDTO.setMovie(movieMapper.toResponseDTO(entity.get()));
            movieReviewDTO.setReviews(entity.get().getReviews().stream().map(reviewMapper::toDTO).toList());
            return Optional.of(movieReviewDTO);
        }

        return Optional.empty();
    }

    @Override
    public MovieResponseDTO addMovie(MovieRequestDTO movie) {
        return movieMapper.toResponseDTO(movieRepository.save(movieMapper.toEntity(movie)));
    }

    @Override
    public Optional<MovieResponseDTO> updateMovie(long id, MovieRequestDTO movie) {
        Optional<MovieEntity> entity = movieRepository.findById(id);

        if (entity.isPresent()) {
            MovieEntity updatedEntity = entity.get();
            updatedEntity.setTitle(movie.getTitle());
            updatedEntity.setGenre(movie.getGenre());
            updatedEntity.setReleaseYear(movie.getReleaseYear());
            updatedEntity.setDirector(movie.getDirector());
            updatedEntity.setRating(movie.getRating());

            entity = Optional.of(movieRepository.save(updatedEntity));
        }

        return entity.map(movieMapper::toResponseDTO);
    }

    @Override
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    @Override
    public PagedModel<MovieResponseDTO> searchMovies(
            String title,
            String genre,
            Integer year,
            String director,
            Double minRating,
            Double maxRating,
            Pageable pageable
    ) {
        return new PagedModel<>(movieRepository.findAll(getSearchSpecification(title, genre, year, director, minRating, maxRating), pageable)
                .map(movieMapper::toResponseDTO));
    }

    private Specification<MovieEntity> getSearchSpecification(String title,
                                                              String genre,
                                                              Integer year,
                                                              String director,
                                                              Double minRating,
                                                              Double maxRating) {
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
