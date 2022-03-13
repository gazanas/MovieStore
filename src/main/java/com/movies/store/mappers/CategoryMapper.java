package com.movies.store.mappers;

import com.movies.store.dtos.CategoryOutDto;
import com.movies.store.models.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "category", target = "category")
    CategoryOutDto categoryToOutputDto(Category category);
}
