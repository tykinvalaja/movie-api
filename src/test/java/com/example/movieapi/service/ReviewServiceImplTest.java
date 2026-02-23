package com.example.movieapi.service;

import com.example.movieapi.entity.MovieEntity;
import com.example.movieapi.entity.ReviewEntity;
import com.example.movieapi.mapper.ReviewMapper;
import com.example.movieapi.model.ReviewResponseDTO;
import com.example.movieapi.model.ReviewRequestDTO;
import com.example.movieapi.repository.MovieRepository;
import com.example.movieapi.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Test
    void addReviewSavesReviewWhenMovieExists() {
        ReviewRequestDTO input = new ReviewRequestDTO("Alice", "Nice", 8);
        MovieEntity movie = new MovieEntity();
        movie.setId(4L);

        ReviewEntity reviewEntity = new ReviewEntity();
        ReviewEntity saved = new ReviewEntity();
        saved.setMovie(movie);

        ReviewResponseDTO output = new ReviewResponseDTO("Alice", "Nice", 8, 4L);

        when(movieRepository.findById(4L)).thenReturn(Optional.of(movie));
        when(reviewMapper.toEntity(input)).thenReturn(reviewEntity);
        when(reviewRepository.save(reviewEntity)).thenReturn(saved);
        when(reviewMapper.toDTO(saved)).thenReturn(output);

        ReviewResponseDTO result = reviewService.addReview(input, 4L);

        assertThat(result).isEqualTo(output);

        ArgumentCaptor<ReviewEntity> reviewCaptor = ArgumentCaptor.forClass(ReviewEntity.class);
        verify(reviewRepository).save(reviewCaptor.capture());
        assertThat(reviewCaptor.getValue().getMovie()).isEqualTo(movie);
    }

    @Test
    void addReviewThrowsNotFoundWhenMovieMissing() {
        ReviewRequestDTO input = new ReviewRequestDTO("Alice", "Nice", 8);
        when(movieRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.addReview(input, 99L))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> {
                    ResponseStatusException responseStatusException = (ResponseStatusException) ex;
                    assertThat(responseStatusException.getStatusCode()).isEqualTo(NOT_FOUND);
                    assertThat(responseStatusException.getReason()).contains("Movie not found with id 99");
                });

        verify(reviewMapper, never()).toEntity(any());
        verify(reviewRepository, never()).save(any());
    }
}
