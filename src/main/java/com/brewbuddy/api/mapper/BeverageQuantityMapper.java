package com.brewbuddy.api.mapper;

import com.brewbuddy.api.dto.BeverageQuantityDto;
import com.brewbuddy.api.dto.BeverageQuantityUpdateDto;
import com.brewbuddy.api.dto.BeverageQuantityCreateDto;
import com.brewbuddy.domain.BeverageQuantityEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface BeverageQuantityMapper {

    BeverageQuantityDto toDto(BeverageQuantityEntity entity);

    @Mapping(target = "beverage", ignore = true)
    BeverageQuantityEntity toEntity(BeverageQuantityCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "beverageId", ignore = true)
    @Mapping(target = "beverage", ignore = true)
    void updateEntityFromDto(BeverageQuantityUpdateDto dto, @MappingTarget BeverageQuantityEntity entity);
}