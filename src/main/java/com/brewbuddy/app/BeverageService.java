package com.brewbuddy.app;

import com.brewbuddy.api.dto.BeverageCreateDto;
import com.brewbuddy.api.dto.BeverageDto;
import com.brewbuddy.api.dto.BeverageUpdateDto;
import com.brewbuddy.api.mapper.BeverageMapper;
import com.brewbuddy.domain.BeverageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BeverageService {

    private final BeverageRepository beverageRepository;
    private final BeverageMapper beverageMapper;

    //TODO add filters
    public Page<BeverageDto> list(String type, String nameContains, String brand, Pageable pageable) {
        Page<BeverageEntity> page = beverageRepository.findAll(pageable);
        return page.map(beverageMapper::toDto);
    }

    public BeverageDto get(UUID id) {
        BeverageEntity entity = beverageRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Beverage not found"));
        return beverageMapper.toDto(entity);
    }

    public BeverageDto create(BeverageCreateDto dto) {
        BeverageEntity entity = beverageMapper.toEntity(dto);
        entity.setCreatedAt(OffsetDateTime.from(Instant.now()));
        entity.setUpdatedAt(OffsetDateTime.from(Instant.now()));

        BeverageEntity saved = beverageRepository.save(entity);
        return beverageMapper.toDto(saved);
    }

    public BeverageDto update(UUID id, BeverageUpdateDto dto) {
        BeverageEntity entity = beverageRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Beverage not found"));

        beverageMapper.updateEntityFromDto(dto, entity);
        entity.setUpdatedAt(OffsetDateTime.from(Instant.now()));

        BeverageEntity saved = beverageRepository.save(entity);
        return beverageMapper.toDto(saved);
    }

    public void delete(UUID id) {
        if (!beverageRepository.existsById(id)) {
            throw new NoSuchElementException("Beverage not found");
        }
        beverageRepository.deleteById(id);
    }
}
