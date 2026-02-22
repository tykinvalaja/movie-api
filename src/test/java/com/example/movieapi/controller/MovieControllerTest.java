package com.example.movieapi.controller;

import com.example.movieapi.model.MovieRequestDTO;
import com.example.movieapi.model.MovieResponseDTO;
import com.example.movieapi.model.MovieReviewDTO;
import com.example.movieapi.model.ReviewDTO;
import com.example.movieapi.service.MovieServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MovieControllerTest {

    @Mock
    private MovieServiceImpl movieService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(new MovieController(movieService)).build();
    }

    @Test
    void getMoviesReturnsPagedMovies() throws Exception {
        MovieResponseDTO movie = new MovieResponseDTO(1L, "The Matrix", "Sci-Fi", 1999, "Wachowski", 8.7);
        Page<MovieResponseDTO> page = new PageImpl<>(List.of(movie));
        when(movieService.getAllMovies(any(Pageable.class))).thenReturn(new PagedModel<>(page));

        mockMvc.perform(get("/movies")
                        .param("page", "2")
                        .param("size", "3")
                        .param("sortBy", "rating")
                        .param("direction", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].title").value("The Matrix"));

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(movieService).getAllMovies(pageableCaptor.capture());

        Pageable pageable = pageableCaptor.getValue();
        assertThat(pageable.getPageNumber()).isEqualTo(2);
        assertThat(pageable.getPageSize()).isEqualTo(3);
        assertThat(pageable.getSort().getOrderFor("rating").getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    void getMovieReturnsMovieWhenFound() throws Exception {
        MovieResponseDTO movie = new MovieResponseDTO(5L, "Inception", "Sci-Fi", 2010, "Nolan", 8.8);
        ReviewDTO review = new ReviewDTO("John", "Great", 9, 5L);
        when(movieService.getMovie(5L)).thenReturn(Optional.of(new MovieReviewDTO(movie, List.of(review))));

        mockMvc.perform(get("/movies/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movie.id").value(5L))
                .andExpect(jsonPath("$.reviews[0].author").value("John"));
    }

    @Test
    void getMovieReturnsNotFoundWhenMissing() throws Exception {
        when(movieService.getMovie(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/movies/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addMovieReturnsCreatedMovie() throws Exception {
        MovieRequestDTO request = new MovieRequestDTO("Dune", "Sci-Fi", 2021, "Villeneuve", 8.0);
        MovieResponseDTO response = new MovieResponseDTO(10L, "Dune", "Sci-Fi", 2021, "Villeneuve", 8.0);
        when(movieService.addMovie(any(MovieRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.title").value("Dune"));
    }

    @Test
    void updateMovieReturnsNotFoundWhenMovieMissing() throws Exception {
        MovieRequestDTO request = new MovieRequestDTO("Dune", "Sci-Fi", 2021, "Villeneuve", 8.0);
        when(movieService.updateMovie(eq(10L), any(MovieRequestDTO.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/movies/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteMovieReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/movies/3"))
                .andExpect(status().isNoContent());

        verify(movieService).deleteMovie(3L);
    }

    @Test
    void searchMoviesPassesFiltersAndReturnsResult() throws Exception {
        MovieResponseDTO movie = new MovieResponseDTO(2L, "Interstellar", "Sci-Fi", 2014, "Nolan", 8.6);
        Page<MovieResponseDTO> page = new PageImpl<>(List.of(movie));
        when(movieService.searchMovies(any(), any(), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(new PagedModel<>(page));

        mockMvc.perform(get("/movies/search")
                        .param("title", "Inter")
                        .param("genre", "Sci")
                        .param("releaseYear", "2014")
                        .param("director", "Nolan")
                        .param("minRating", "7.0")
                        .param("maxRating", "9.0")
                        .param("page", "1")
                        .param("size", "4")
                        .param("sortBy", "title")
                        .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Interstellar"));

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(movieService).searchMovies(
                eq("Inter"),
                eq("Sci"),
                eq(2014),
                eq("Nolan"),
                eq(7.0),
                eq(9.0),
                pageableCaptor.capture()
        );

        Pageable pageable = pageableCaptor.getValue();
        assertThat(pageable.getPageNumber()).isEqualTo(1);
        assertThat(pageable.getPageSize()).isEqualTo(4);
        assertThat(pageable.getSort().getOrderFor("title").getDirection()).isEqualTo(Sort.Direction.ASC);
    }
}
