package com.brewbuddy.app;

import com.brewbuddy.api.dto.BeverageQuantityDto;
import com.brewbuddy.api.dto.BeverageQuantityUpdateDto;
import com.brewbuddy.api.dto.BeverageQuantityCreateDto;
import com.brewbuddy.api.mapper.BeverageQuantityMapper;
import com.brewbuddy.domain.BeverageQuantityEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BeverageQuantityService {

    private final BeverageQuantityRepository beverageQuantityRepository;
    private final BeverageQuantityMapper beverageQuantityMapper;

    public BeverageQuantityDto get(UUID id) {
        BeverageQuantityEntity entity = beverageQuantityRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Beverage has no quantity"));
        return beverageQuantityMapper.toDto(entity);
    }

    public BeverageQuantityDto create(BeverageQuantityCreateDto dto) {
        BeverageQuantityEntity entity = beverageQuantityMapper.toEntity(dto);

        BeverageQuantityEntity saved = beverageQuantityRepository.save(entity);
        return beverageQuantityMapper.toDto(saved);
    }

    public BeverageQuantityDto update(UUID id, BeverageQuantityUpdateDto dto) {
        BeverageQuantityEntity entity = beverageQuantityRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Beverage has no quantity"));

        beverageQuantityMapper.updateEntityFromDto(dto, entity);

        BeverageQuantityEntity saved = beverageQuantityRepository.save(entity);
        return beverageQuantityMapper.toDto(saved);
    }

    public void delete(UUID id) {
        if (!beverageQuantityRepository.existsById(id)) {
            throw new NoSuchElementException("Beverage has no quantity");
        }
        beverageQuantityRepository.deleteById(id);
    }
}
