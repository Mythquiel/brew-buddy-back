package com.brewbuddy.api.mapper;

import com.brewbuddy.api.dto.BeverageCreateDto;
import com.brewbuddy.api.dto.BeverageDto;
import com.brewbuddy.api.dto.BeverageUpdateDto;
import com.brewbuddy.domain.BeverageEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface BeverageMapper {

    BeverageDto toDto(BeverageEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "beverageQuantity", ignore = true)
    @Mapping(target = "brewLogs", ignore = true)
    BeverageEntity toEntity(BeverageCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "beverageQuantity", ignore = true)
    @Mapping(target = "brewLogs", ignore = true)
    void updateEntityFromDto(BeverageUpdateDto dto, @MappingTarget BeverageEntity entity);
}