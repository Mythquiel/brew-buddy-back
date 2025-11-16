package com.brewbuddy.app;

import com.brewbuddy.api.dto.BrewLogCreateDto;
import com.brewbuddy.api.dto.BrewLogDto;
import com.brewbuddy.api.dto.BrewLogUpdateDto;
import com.brewbuddy.api.mapper.BrewLogMapper;
import com.brewbuddy.domain.BrewLogEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BrewLogService {

    private final BrewLogRepository brewLogRepository;
    private final BrewLogMapper brewLogMapper;

    //TODO add filters
    public Page<BrewLogDto> list(Pageable pageable) {
        Page<BrewLogEntity> page = brewLogRepository.findAll(pageable);
        return page.map(brewLogMapper::toDto);
    }

    public BrewLogDto get(UUID id) {
        BrewLogEntity entity = brewLogRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("BrewLog not found"));
        return brewLogMapper.toDto(entity);
    }

    public BrewLogDto create(BrewLogCreateDto dto) {
        BrewLogEntity entity = brewLogMapper.toEntity(dto);

        BrewLogEntity saved = brewLogRepository.save(entity);
        return brewLogMapper.toDto(saved);
    }

    public BrewLogDto update(UUID id, BrewLogUpdateDto dto) {
        BrewLogEntity entity = brewLogRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("BrewLog not found"));

        brewLogMapper.updateEntityFromDto(dto, entity);

        BrewLogEntity saved = brewLogRepository.save(entity);
        return brewLogMapper.toDto(saved);
    }

    public void delete(UUID id) {
        if (!brewLogRepository.existsById(id)) {
            throw new NoSuchElementException("BrewLog not found");
        }
        brewLogRepository.deleteById(id);
    }
}
