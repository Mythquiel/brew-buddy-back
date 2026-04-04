package com.brewbuddy.app;

import com.brewbuddy.api.dto.BrewLogCreateDto;
import com.brewbuddy.api.dto.BrewLogDto;
import com.brewbuddy.api.dto.BrewLogUpdateDto;
import com.brewbuddy.api.mapper.BrewLogMapper;
import com.brewbuddy.domain.BeverageEntity;
import com.brewbuddy.domain.BrewLogEntity;
import com.brewbuddy.domain.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("BrewLogService Tests")
class BrewLogServiceTest {

    @Mock
    private BrewLogRepository brewLogRepository;

    @Mock
    private BrewLogMapper brewLogMapper;

    @InjectMocks
    private BrewLogService brewLogService;

    private UUID testId;
    private UUID beverageId;
    private UUID userId;
    private BrewLogEntity testEntity;
    private BrewLogDto testDto;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        beverageId = UUID.randomUUID();
        userId = UUID.randomUUID();

        BeverageEntity beverage = new BeverageEntity();
        beverage.setId(beverageId);

        UserEntity user = new UserEntity();
        user.setId(userId);

        testEntity = BrewLogEntity.builder()
                .id(testId)
                .beverage(beverage)
                .user(user)
                .amountUsed(250)
                .brewedAt(OffsetDateTime.now())
                .build();

        testDto = BrewLogDto.builder()
                .id(testId)
                .beverageId(beverageId)
                .userId(userId)
                .amountUsed(250)
                .brewedAt(OffsetDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should list all brew logs with filters")
    void shouldListBrewLogsWithFilters() {
        // Given
        Pageable pageable = PageRequest.of(0, 20);
        Page<BrewLogEntity> entityPage = new PageImpl<>(List.of(testEntity));
        when(brewLogRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(entityPage);
        when(brewLogMapper.toDto(testEntity)).thenReturn(testDto);

        // When
        Page<BrewLogDto> result = brewLogService.list(beverageId, null, null, userId, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(testId);
        verify(brewLogRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    @DisplayName("Should get brew log by id")
    void shouldGetBrewLogById() {
        // Given
        when(brewLogRepository.findById(testId)).thenReturn(Optional.of(testEntity));
        when(brewLogMapper.toDto(testEntity)).thenReturn(testDto);

        // When
        BrewLogDto result = brewLogService.get(testId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testId);
        assertThat(result.getBeverageId()).isEqualTo(beverageId);
        assertThat(result.getUserId()).isEqualTo(userId);
        verify(brewLogRepository).findById(testId);
    }

    @Test
    @DisplayName("Should throw NoSuchElementException when brew log not found")
    void shouldThrowExceptionWhenBrewLogNotFound() {
        // Given
        when(brewLogRepository.findById(testId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> brewLogService.get(testId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("BrewLog not found");
    }

    @Test
    @DisplayName("Should create new brew log")
    void shouldCreateBrewLog() {
        // Given
        BrewLogCreateDto createDto = BrewLogCreateDto.builder()
                .beverageId(beverageId)
                .userId(userId)
                .amountUsed(250)
                .build();

        when(brewLogMapper.toEntity(createDto)).thenReturn(testEntity);
        when(brewLogRepository.save(testEntity)).thenReturn(testEntity);
        when(brewLogMapper.toDto(testEntity)).thenReturn(testDto);

        // When
        BrewLogDto result = brewLogService.create(createDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testId);
        assertThat(result.getAmountUsed()).isEqualTo(250);
        verify(brewLogRepository).save(testEntity);
    }

    @Test
    @DisplayName("Should update existing brew log")
    void shouldUpdateBrewLog() {
        // Given
        BrewLogUpdateDto updateDto = BrewLogUpdateDto.builder()
                .amountUsed(300)
                .build();

        when(brewLogRepository.findById(testId)).thenReturn(Optional.of(testEntity));
        when(brewLogRepository.save(testEntity)).thenReturn(testEntity);
        when(brewLogMapper.toDto(testEntity)).thenReturn(testDto);

        // When
        BrewLogDto result = brewLogService.update(testId, updateDto);

        // Then
        assertThat(result).isNotNull();
        verify(brewLogMapper).updateEntityFromDto(updateDto, testEntity);
        verify(brewLogRepository).save(testEntity);
    }

    @Test
    @DisplayName("Should delete brew log")
    void shouldDeleteBrewLog() {
        // Given
        when(brewLogRepository.existsById(testId)).thenReturn(true);

        // When
        brewLogService.delete(testId);

        // Then
        verify(brewLogRepository).existsById(testId);
        verify(brewLogRepository).deleteById(testId);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent brew log")
    void shouldThrowExceptionWhenDeletingNonExistentBrewLog() {
        // Given
        when(brewLogRepository.existsById(testId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> brewLogService.delete(testId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("BrewLog not found");
    }
}
