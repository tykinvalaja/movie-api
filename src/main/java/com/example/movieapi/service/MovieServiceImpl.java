package com.example.movieapi.service;

import com.example.movieapi.entity.MovieEntity;
import com.example.movieapi.mapper.MovieMapper;
import com.example.movieapi.model.Movie;
import com.example.movieapi.repository.MovieRepository;
import com.example.movieapi.repository.MovieSearchSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MoveService{

    private final MovieRepository movieRepository;

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream().map(MovieMapper::toDto).toList();
    }

    @Override
    public Optional<Movie> getMovie(long id) {
        return movieRepository.findById(id).map(MovieMapper::toDto);
    }

    @Override
    public Movie addMovie(Movie movie) {
        return MovieMapper.toDto(movieRepository.save(MovieMapper.toEntity(movie)));
    }

    @Override
    public Optional<Movie> updateMovie(long id, Movie movie) {
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

        return entity.map(MovieMapper::toDto);
    }

    @Override
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    @Override
    public List<Movie> searchMovies(
            String title,
            String genre,
            Integer year,
            String director,
            Double minRating,
            Double maxRating
    ) {
        return movieRepository.findAll(
                        MovieSearchSpecification.search(title, genre, year, director, minRating, maxRating),
                        Sort.by(Sort.Direction.ASC, "id"))
                .stream().map(MovieMapper::toDto).toList();
    }
}
