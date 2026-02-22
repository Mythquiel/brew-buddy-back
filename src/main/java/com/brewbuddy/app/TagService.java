package com.brewbuddy.app;

import com.brewbuddy.api.dto.TagCreateDto;
import com.brewbuddy.api.dto.TagDto;
import com.brewbuddy.api.dto.TagUpdateDto;
import com.brewbuddy.api.mapper.TagMapper;
import com.brewbuddy.domain.TagEntity;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public Page<TagDto> list(String nameContains, Pageable pageable) {
        Specification<TagEntity> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nameContains != null && !nameContains.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + nameContains.toLowerCase() + "%"
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<TagEntity> page = tagRepository.findAll(spec, pageable);
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
