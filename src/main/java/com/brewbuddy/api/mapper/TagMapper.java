package com.brewbuddy.api.mapper;

import com.brewbuddy.api.dto.TagCreateDto;
import com.brewbuddy.api.dto.TagDto;
import com.brewbuddy.api.dto.TagUpdateDto;
import com.brewbuddy.domain.TagEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TagMapper {

    TagDto toDto(TagEntity entity);

    TagEntity toEntity(TagCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(TagUpdateDto dto, @MappingTarget TagEntity entity);
}