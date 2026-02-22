package com.example.movieapi.service;

import com.example.movieapi.entity.MovieEntity;
import com.example.movieapi.mapper.MovieMapper;
import com.example.movieapi.mapper.ReviewMapper;
import com.example.movieapi.model.MovieRequestDTO;
import com.example.movieapi.model.MovieResponseDTO;
import com.example.movieapi.model.MovieReviewDTO;
import com.example.movieapi.repository.MovieRepository;
import com.example.movieapi.repository.MovieSearchSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MoveService{

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final ReviewMapper reviewMapper;

    @Override
    public Page<MovieResponseDTO> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable).map(movieMapper::toResponseDTO);
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
    public Page<MovieResponseDTO> searchMovies(
            String title,
            String genre,
            Integer year,
            String director,
            Double minRating,
            Double maxRating,
            Pageable pageable
    ) {
        return movieRepository.findAll(
                        MovieSearchSpecification.search(title, genre, year, director, minRating, maxRating),
                        pageable)
                .map(movieMapper::toResponseDTO);
    }
}
