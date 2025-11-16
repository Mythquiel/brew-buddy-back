package com.brewbuddy.app;

import com.brewbuddy.api.dto.TagCreateDto;
import com.brewbuddy.api.dto.TagDto;
import com.brewbuddy.api.dto.TagUpdateDto;
import com.brewbuddy.api.mapper.TagMapper;
import com.brewbuddy.domain.TagEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    //TODO add filters
    public Page<TagDto> list(Pageable pageable) {
        Page<TagEntity> page = tagRepository.findAll(pageable);
        return page.map(tagMapper::toDto);
    }

    public TagDto get(UUID id) {
        TagEntity entity = tagRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tag not found"));
        return tagMapper.toDto(entity);
    }

    public TagDto create(TagCreateDto dto) {
        TagEntity entity = tagMapper.toEntity(dto);

        TagEntity saved = tagRepository.save(entity);
        return tagMapper.toDto(saved);
    }

    public TagDto update(UUID id, TagUpdateDto dto) {
        TagEntity entity = tagRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tag not found"));
        
        tagMapper.updateEntityFromDto(dto, entity);

        TagEntity saved = tagRepository.save(entity);
        return tagMapper.toDto(saved);
    }

    public void delete(UUID id) {
        if (!tagRepository.existsById(id)) {
            throw new NoSuchElementException("Tag not found");
        }
        tagRepository.deleteById(id);
    }
}
