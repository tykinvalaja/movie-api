package com.example.movieapi.mapper;

import com.example.movieapi.entity.ReviewEntity;
import com.example.movieapi.model.ReviewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movie", ignore = true)
    ReviewEntity toEntity(ReviewDTO reviewDTO);

    @Mapping(target = "movieId", source = "movie.id")
    ReviewDTO toDTO(ReviewEntity reviewEntity);
}
