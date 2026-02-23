package com.example.movieapi.mapper;

import com.example.movieapi.entity.ReviewEntity;
import com.example.movieapi.model.ReviewResponseDTO;
import com.example.movieapi.model.ReviewRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(target = "movie", ignore = true)
    ReviewEntity toEntity(ReviewRequestDTO reviewDTO);

    @Mapping(target = "movieId", source = "movie.id")
    ReviewResponseDTO toDTO(ReviewEntity reviewEntity);
}
