package com.example.movieapi.mapper;

import com.example.movieapi.entity.MovieEntity;
import com.example.movieapi.model.MovieRequestDTO;
import com.example.movieapi.model.MovieResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    @Mapping(target = "id", ignore = true)
    MovieEntity toEntity(MovieRequestDTO requestDTO);
    MovieResponseDTO toResponseDTO(MovieEntity entity);
}
