package com.movies.store.mappers;

import com.movies.store.dtos.ChargeDto;
import com.movies.store.models.Charge;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ChargeMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "paid", target = "paid")
    @Mapping(source = "rentedCopy.copy.id", target = "copyId")
    ChargeDto chargeToDto(Charge charge);
}
