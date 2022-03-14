package com.movies.store.mappers;

import com.movies.store.dtos.InformationDto;
import com.movies.store.models.Information;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface InformationMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "summary", target = "summary")
    @Mapping(source = "released", target = "released")
    @Mapping(source = "rating", target = "rating")
    @Mapping(source = "actors", target = "actors")
    @Mapping(source = "directors", target = "directors")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    InformationDto informationToOutputDto(Information information);
}
