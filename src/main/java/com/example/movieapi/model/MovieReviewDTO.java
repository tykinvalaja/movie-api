package com.example.movieapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieReviewDTO {
    private MovieResponseDTO movie;
    private List<ReviewDTO> reviews;
}
