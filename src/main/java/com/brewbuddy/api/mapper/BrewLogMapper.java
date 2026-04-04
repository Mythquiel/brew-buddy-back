package com.brewbuddy.api.mapper;

import com.brewbuddy.api.dto.BrewLogCreateDto;
import com.brewbuddy.api.dto.BrewLogDto;
import com.brewbuddy.api.dto.BrewLogUpdateDto;
import com.brewbuddy.domain.BrewLogEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface BrewLogMapper {

    @Mapping(source = "beverage.id", target = "beverageId")
    @Mapping(source = "user.id", target = "userId")
    BrewLogDto toDto(BrewLogEntity entity);

    @Mapping(source = "beverageId", target = "beverage.id")
    @Mapping(source = "userId", target = "user.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "brewedAt", ignore = true)
    BrewLogEntity toEntity(BrewLogCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "beverage", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "brewedAt", ignore = true)
    void updateEntityFromDto(BrewLogUpdateDto dto, @MappingTarget BrewLogEntity entity);
}