package com.example.movieapi.controller;

import com.example.movieapi.model.ReviewRequestDTO;
import com.example.movieapi.model.ReviewResponseDTO;
import com.example.movieapi.service.MovieServiceImpl;
import com.example.movieapi.service.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    @Mock
    private ReviewServiceImpl reviewService;

    @Mock
    private MovieServiceImpl movieService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new MovieController(movieService, reviewService)).build();
    }

    @Test
    void addReviewReturnsCreatedReview() throws Exception {
        ReviewRequestDTO request = new ReviewRequestDTO("John", "Great movie", 9);
        ReviewResponseDTO response = new ReviewResponseDTO("John", "Great movie", 9, 1L);

        when(reviewService.addReview(any(ReviewRequestDTO.class), eq(1L))).thenReturn(response);

        mockMvc.perform(post("/movies/1/review")
                        .param("author", request.getAuthor())
                        .param("content", request.getContent())
                        .param("rating", String.valueOf(request.getRating())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.author").value("John"))
                .andExpect(jsonPath("$.movieId").value(1L));
    }
}
