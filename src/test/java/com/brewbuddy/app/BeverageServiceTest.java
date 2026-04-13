package com.brewbuddy.app;

import com.brewbuddy.api.dto.BeverageCreateDto;
import com.brewbuddy.api.dto.BeverageDto;
import com.brewbuddy.api.dto.BeverageUpdateDto;
import com.brewbuddy.api.mapper.BeverageMapper;
import com.brewbuddy.domain.BeverageEntity;
import com.brewbuddy.domain.BeverageType;
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
@DisplayName("BeverageService Tests")
class BeverageServiceTest {

    @Mock
    private BeverageRepository beverageRepository;

    @Mock
    private BeverageMapper beverageMapper;

    @Mock
    private SupabaseStorageService storageService;

    @InjectMocks
    private BeverageService beverageService;

    private UUID testId;
    private BeverageEntity testEntity;
    private BeverageDto testDto;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();

        testEntity = BeverageEntity.builder()
                .id(testId)
                .type(BeverageType.TEA)
                .name("Earl Grey")
                .brand("Twinings")
                .brewTimeMinSec(180)
                .brewTimeMaxSec(300)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        testDto = BeverageDto.builder()
                .id(testId)
                .type(BeverageType.TEA)
                .name("Earl Grey")
                .brand("Twinings")
                .brewTimeMinSec(180)
                .brewTimeMaxSec(300)
                .build();
    }

    @Test
    @DisplayName("Should list all beverages with filters")
    void shouldListBeveragesWithFilters() {
        // Given
        Pageable pageable = PageRequest.of(0, 20);
        Page<BeverageEntity> entityPage = new PageImpl<>(List.of(testEntity));
        when(beverageRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(entityPage);
        when(beverageMapper.toDto(testEntity)).thenReturn(testDto);

        // When
        Page<BeverageDto> result = beverageService.list("TEA", "earl", "twinings", pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Earl Grey");
        verify(beverageRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    @DisplayName("Should get beverage by id")
    void shouldGetBeverageById() {
        // Given
        when(beverageRepository.findById(testId)).thenReturn(Optional.of(testEntity));
        when(beverageMapper.toDto(testEntity)).thenReturn(testDto);

        // When
        BeverageDto result = beverageService.get(testId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testId);
        assertThat(result.getName()).isEqualTo("Earl Grey");
        assertThat(result.getType()).isEqualTo(BeverageType.TEA);
        verify(beverageRepository).findById(testId);
    }

    @Test
    @DisplayName("Should throw NoSuchElementException when beverage not found")
    void shouldThrowExceptionWhenBeverageNotFound() {
        // Given
        when(beverageRepository.findById(testId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> beverageService.get(testId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Beverage not found");
    }

    @Test
    @DisplayName("Should create new beverage")
    void shouldCreateBeverage() {
        // Given
        BeverageCreateDto createDto = BeverageCreateDto.builder()
                .type(BeverageType.TEA)
                .name("Earl Grey")
                .brand("Twinings")
                .brewTimeMinSec(180)
                .brewTimeMaxSec(300)
                .build();

        when(beverageMapper.toEntity(createDto)).thenReturn(testEntity);
        when(beverageRepository.save(any(BeverageEntity.class))).thenReturn(testEntity);
        when(beverageMapper.toDto(testEntity)).thenReturn(testDto);

        // When
        BeverageDto result = beverageService.create(createDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Earl Grey");
        verify(beverageRepository).save(any(BeverageEntity.class));
    }

    @Test
    @DisplayName("Should update existing beverage")
    void shouldUpdateBeverage() {
        // Given
        BeverageUpdateDto updateDto = BeverageUpdateDto.builder()
                .name("Updated Earl Grey")
                .build();

        when(beverageRepository.findById(testId)).thenReturn(Optional.of(testEntity));
        when(beverageRepository.save(any(BeverageEntity.class))).thenReturn(testEntity);
        when(beverageMapper.toDto(testEntity)).thenReturn(testDto);

        // When
        BeverageDto result = beverageService.update(testId, updateDto);

        // Then
        assertThat(result).isNotNull();
        verify(beverageMapper).updateEntityFromDto(updateDto, testEntity);
        verify(beverageRepository).save(any(BeverageEntity.class));
    }

    @Test
    @DisplayName("Should delete beverage")
    void shouldDeleteBeverage() {
        // Given
        when(beverageRepository.findById(testId)).thenReturn(Optional.of(testEntity));

        // When
        beverageService.delete(testId);

        // Then
        verify(beverageRepository).findById(testId);
        verify(beverageRepository).delete(testEntity);
    }

    @Test
    @DisplayName("Should delete stored image when deleting beverage")
    void shouldDeleteStoredImageWhenDeletingBeverage() {
        // Given
        testEntity.setImageUrl("beverage-icon/test-image.png");
        when(beverageRepository.findById(testId)).thenReturn(Optional.of(testEntity));

        // When
        beverageService.delete(testId);

        // Then
        verify(storageService).delete("beverage-icon", "test-image.png");
        verify(beverageRepository).delete(testEntity);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent beverage")
    void shouldThrowExceptionWhenDeletingNonExistentBeverage() {
        // Given
        when(beverageRepository.findById(testId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> beverageService.delete(testId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Beverage not found");
    }

    @Test
    @DisplayName("Should get signed image URL")
    void shouldGetSignedImageUrl() {
        // Given
        testEntity.setImageUrl("beverage-icon/test-image.png");
        when(beverageRepository.findById(testId)).thenReturn(Optional.of(testEntity));
        when(storageService.generateSignedUrl("beverage-icon", "test-image.png"))
                .thenReturn("https://signed-url.com/test-image.png");

        // When
        String result = beverageService.getSignedImageUrl(testId);

        // Then
        assertThat(result).isEqualTo("https://signed-url.com/test-image.png");
        verify(storageService).generateSignedUrl("beverage-icon", "test-image.png");
    }

    @Test
    @DisplayName("Should throw exception when getting image URL for beverage without image")
    void shouldThrowExceptionWhenGettingImageUrlWithoutImage() {
        // Given
        testEntity.setImageUrl(null);
        when(beverageRepository.findById(testId)).thenReturn(Optional.of(testEntity));

        // When & Then
        assertThatThrownBy(() -> beverageService.getSignedImageUrl(testId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Beverage has no image");
    }
}
