package com.brewbuddy.api.mapper;

import com.brewbuddy.api.dto.BeverageCreateDto;
import com.brewbuddy.api.dto.BeverageDto;
import com.brewbuddy.api.dto.BeverageUpdateDto;
import com.brewbuddy.domain.BeverageEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface BeverageMapper {

    BeverageDto toDto(BeverageEntity entity);

    BeverageEntity toEntity(BeverageCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(BeverageUpdateDto dto, @MappingTarget BeverageEntity entity);
}