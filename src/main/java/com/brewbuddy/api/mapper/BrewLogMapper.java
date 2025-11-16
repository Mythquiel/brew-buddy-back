package com.brewbuddy.api.mapper;

import com.brewbuddy.api.dto.BrewLogCreateDto;
import com.brewbuddy.api.dto.BrewLogDto;
import com.brewbuddy.api.dto.BrewLogUpdateDto;
import com.brewbuddy.domain.BrewLogEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface BrewLogMapper {

    BrewLogDto toDto(BrewLogEntity entity);

    BrewLogEntity toEntity(BrewLogCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(BrewLogUpdateDto dto, @MappingTarget BrewLogEntity entity);
}