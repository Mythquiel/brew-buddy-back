package com.brewbuddy.api.mapper;

import com.brewbuddy.api.dto.TagCreateDto;
import com.brewbuddy.api.dto.TagDto;
import com.brewbuddy.api.dto.TagUpdateDto;
import com.brewbuddy.domain.TagEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TagMapper {

    TagDto toDto(TagEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "beverages", ignore = true)
    TagEntity toEntity(TagCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "beverages", ignore = true)
    void updateEntityFromDto(TagUpdateDto dto, @MappingTarget TagEntity entity);
}