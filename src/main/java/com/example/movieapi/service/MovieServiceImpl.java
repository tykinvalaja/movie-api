package com.example.movieapi.service;

import com.example.movieapi.entity.MovieEntity;
import com.example.movieapi.mapper.MovieMapper;
import com.example.movieapi.model.MovieRequestDTO;
import com.example.movieapi.model.MovieResponseDTO;
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
    private final MovieMapper movieMapper;

    @Override
    public List<MovieResponseDTO> getAllMovies() {
        return movieRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream().map(movieMapper::toResponseDTO).toList();
    }

    @Override
    public Optional<MovieResponseDTO> getMovie(long id) {
        return movieRepository.findById(id).map(movieMapper::toResponseDTO);
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
    public List<MovieResponseDTO> searchMovies(
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
                .stream().map(movieMapper::toResponseDTO).toList();
    }
}
