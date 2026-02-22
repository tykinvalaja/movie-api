package com.example.movieapi.service;

import com.example.movieapi.entity.MovieEntity;
import com.example.movieapi.entity.ReviewEntity;
import com.example.movieapi.mapper.MovieMapper;
import com.example.movieapi.mapper.ReviewMapper;
import com.example.movieapi.model.MovieRequestDTO;
import com.example.movieapi.model.MovieResponseDTO;
import com.example.movieapi.model.MovieReviewDTO;
import com.example.movieapi.model.ReviewDTO;
import com.example.movieapi.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MovieMapper movieMapper;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private MovieServiceImpl movieService;

    @Test
    void getMovieReturnsMovieAndReviewsWhenMovieExists() {
        MovieEntity movieEntity = new MovieEntity();
        movieEntity.setId(7L);

        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setMovie(movieEntity);
        movieEntity.setReviews(Set.of(reviewEntity));

        MovieResponseDTO movieResponseDTO = new MovieResponseDTO(7L, "The Matrix", "Sci-Fi", 1999, "Wachowski", 8.7);
        ReviewDTO reviewDTO = new ReviewDTO("John", "Great", 9, 7L);

        when(movieRepository.findById(7L)).thenReturn(Optional.of(movieEntity));
        when(movieMapper.toResponseDTO(movieEntity)).thenReturn(movieResponseDTO);
        when(reviewMapper.toDTO(reviewEntity)).thenReturn(reviewDTO);

        Optional<MovieReviewDTO> result = movieService.getMovie(7L);

        assertThat(result).isPresent();
        assertThat(result.get().getMovie()).isEqualTo(movieResponseDTO);
        assertThat(result.get().getReviews()).containsExactly(reviewDTO);
    }

    @Test
    void getMovieReturnsEmptyWhenMovieDoesNotExist() {
        when(movieRepository.findById(22L)).thenReturn(Optional.empty());

        Optional<MovieReviewDTO> result = movieService.getMovie(22L);

        assertThat(result).isEmpty();
        verify(movieMapper, never()).toResponseDTO(any());
        verify(reviewMapper, never()).toDTO(any());
    }

    @Test
    void addMovieMapsAndSavesMovie() {
        MovieRequestDTO requestDTO = new MovieRequestDTO("Inception", "Sci-Fi", 2010, "Nolan", 8.8);
        MovieEntity toSave = new MovieEntity();
        MovieEntity saved = new MovieEntity();
        saved.setId(10L);
        MovieResponseDTO responseDTO = new MovieResponseDTO(10L, "Inception", "Sci-Fi", 2010, "Nolan", 8.8);

        when(movieMapper.toEntity(requestDTO)).thenReturn(toSave);
        when(movieRepository.save(toSave)).thenReturn(saved);
        when(movieMapper.toResponseDTO(saved)).thenReturn(responseDTO);

        MovieResponseDTO result = movieService.addMovie(requestDTO);

        assertThat(result).isEqualTo(responseDTO);
        verify(movieRepository).save(toSave);
    }

    @Test
    void updateMovieUpdatesExistingMovie() {
        MovieRequestDTO requestDTO = new MovieRequestDTO("New Title", "Drama", 2020, "Jane Doe", 7.2);
        MovieEntity existing = new MovieEntity();
        existing.setId(5L);
        existing.setTitle("Old Title");
        existing.setGenre("Action");
        existing.setReleaseYear(2001);
        existing.setDirector("John Doe");
        existing.setRating(5.0);

        MovieResponseDTO responseDTO = new MovieResponseDTO(5L, "New Title", "Drama", 2020, "Jane Doe", 7.2);

        when(movieRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(movieRepository.save(existing)).thenReturn(existing);
        when(movieMapper.toResponseDTO(existing)).thenReturn(responseDTO);

        Optional<MovieResponseDTO> result = movieService.updateMovie(5L, requestDTO);

        assertThat(result).contains(responseDTO);
        assertThat(existing.getTitle()).isEqualTo("New Title");
        assertThat(existing.getGenre()).isEqualTo("Drama");
        assertThat(existing.getReleaseYear()).isEqualTo(2020);
        assertThat(existing.getDirector()).isEqualTo("Jane Doe");
        assertThat(existing.getRating()).isEqualTo(7.2);
        verify(movieRepository).save(existing);
    }

    @Test
    void updateMovieReturnsEmptyWhenMovieDoesNotExist() {
        when(movieRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<MovieResponseDTO> result = movieService.updateMovie(999L, new MovieRequestDTO());

        assertThat(result).isEmpty();
        verify(movieRepository, never()).save(any());
    }

    @Test
    void deleteMovieDelegatesToRepository() {
        movieService.deleteMovie(15L);

        verify(movieRepository).deleteById(15L);
    }

    @Test
    void searchMoviesPassesSpecificationAndPageableToRepository() {
        MovieEntity entity = new MovieEntity();
        entity.setId(3L);
        MovieResponseDTO responseDTO = new MovieResponseDTO(3L, "Interstellar", "Sci-Fi", 2014, "Nolan", 8.6);
        Page<MovieEntity> page = new PageImpl<>(List.of(entity));
        Pageable pageable = PageRequest.of(1, 5, Sort.by("title").descending());

        when(movieRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(movieMapper.toResponseDTO(entity)).thenReturn(responseDTO);

        PagedModel<MovieResponseDTO> result = movieService.searchMovies(
                "Inter",
                "Sci",
                2014,
                "Nolan",
                7.0,
                9.0,
                pageable
        );

        assertThat(result.getContent()).containsExactly(responseDTO);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(movieRepository).findAll(any(Specification.class), pageableCaptor.capture());
        assertThat(pageableCaptor.getValue()).isEqualTo(pageable);
    }
}
