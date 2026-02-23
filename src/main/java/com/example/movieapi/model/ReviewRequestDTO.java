package com.example.movieapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestDTO {
    @Schema(description = "Review author", example = "John")
    private String author;
    @Schema(description = "Review text", example = "Great movie")
    private String content;
    @Schema(description = "Rating from 1 to 10", example = "9")
    private int rating;
}
