package com.movies.store.mappers;

import com.movies.store.dtos.CopyDto;
import com.movies.store.models.RentedCopy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CopyMapper {

    @Mapping(source = "copy.id", target = "id")
    @Mapping(source = "copy.movie.id", target = "movieId")
    CopyDto rentedCopyToDto(RentedCopy rentedCopy);
}
