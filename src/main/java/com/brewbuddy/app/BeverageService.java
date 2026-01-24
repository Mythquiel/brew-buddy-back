package com.brewbuddy.app;

import com.brewbuddy.api.dto.BeverageCreateDto;
import com.brewbuddy.api.dto.BeverageDto;
import com.brewbuddy.api.dto.BeverageUpdateDto;
import com.brewbuddy.api.mapper.BeverageMapper;
import com.brewbuddy.domain.BeverageEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BeverageService {

    private final BeverageRepository beverageRepository;
    private final BeverageMapper beverageMapper;

    //TODO add filters
    public Page<BeverageDto> list(String type, String nameContains, String brand, Pageable pageable) {
        log.debug("Listing beverages with filters - type: {}, nameContains: {}, brand: {}, page: {}",
                type, nameContains, brand, pageable);
        Page<BeverageEntity> page = beverageRepository.findAll(pageable);
        return page.map(beverageMapper::toDto);
    }

    public BeverageDto get(UUID id) {
        log.debug("Getting beverage by id: {}", id);
        BeverageEntity entity = beverageRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Beverage not found with id: {}", id);
                    return new NoSuchElementException("Beverage not found");
                });
        return beverageMapper.toDto(entity);
    }

    public BeverageDto create(BeverageCreateDto dto) {
        BeverageEntity entity = beverageMapper.toEntity(dto);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());

        BeverageEntity saved = beverageRepository.save(entity);
        log.info("Beverage created with id: {}", saved.getId());
        return beverageMapper.toDto(saved);
    }

    public BeverageDto update(UUID id, BeverageUpdateDto dto) {
        log.info("Updating beverage with id: {}", id);
        BeverageEntity entity = beverageRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Beverage not found with id: {}", id);
                    return new NoSuchElementException("Beverage not found");
                });

        beverageMapper.updateEntityFromDto(dto, entity);
        entity.setUpdatedAt(OffsetDateTime.now());

        BeverageEntity saved = beverageRepository.save(entity);
        log.info("Beverage updated with id: {}", id);
        return beverageMapper.toDto(saved);
    }

    public void delete(UUID id) {
        log.info("Deleting beverage with id: {}", id);
        if (!beverageRepository.existsById(id)) {
            log.warn("Beverage not found with id: {}", id);
            throw new NoSuchElementException("Beverage not found");
        }
        beverageRepository.deleteById(id);
        log.info("Beverage deleted with id: {}", id);
    }
}
