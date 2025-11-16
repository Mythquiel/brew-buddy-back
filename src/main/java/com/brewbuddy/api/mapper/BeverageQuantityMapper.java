package com.brewbuddy.api.mapper;

import com.brewbuddy.api.dto.BeverageQuantityDto;
import com.brewbuddy.api.dto.BeverageQuantityUpdateDto;
import com.brewbuddy.api.dto.BeverageQunatityCreateDto;
import com.brewbuddy.domain.BeverageQuantityEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface BeverageQuantityMapper {

    BeverageQuantityDto toDto(BeverageQuantityEntity entity);

    BeverageQuantityEntity toEntity(BeverageQunatityCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(BeverageQuantityUpdateDto dto, @MappingTarget BeverageQuantityEntity entity);
}