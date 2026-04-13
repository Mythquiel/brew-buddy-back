package com.brewbuddy.app;

import com.brewbuddy.api.dto.BeverageCreateDto;
import com.brewbuddy.api.dto.BeverageDto;
import com.brewbuddy.api.dto.BeverageUpdateDto;
import com.brewbuddy.api.mapper.BeverageMapper;
import com.brewbuddy.domain.BeverageEntity;
import com.brewbuddy.domain.BeverageType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BeverageService {

    private static final String IMAGE_BUCKET = "beverage-icon";

    private final BeverageRepository beverageRepository;
    private final BeverageMapper beverageMapper;
    private final SupabaseStorageService storageService;

    public Page<BeverageDto> list(String type, String nameContains, String brand, Pageable pageable) {
        log.debug("Listing beverages with filters - type: {}, nameContains: {}, brand: {}, page: {}",
                type, nameContains, brand, pageable);

        Specification<BeverageEntity> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (type != null && !type.isBlank()) {
                try {
                    BeverageType beverageType = BeverageType.valueOf(type.toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get("type"), beverageType));
                } catch (IllegalArgumentException e) {
                    log.warn("Invalid beverage type: {}", type);
                }
            }

            if (nameContains != null && !nameContains.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + nameContains.toLowerCase() + "%"
                ));
            }

            if (brand != null && !brand.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("brand")),
                        "%" + brand.toLowerCase() + "%"
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<BeverageEntity> page = beverageRepository.findAll(spec, pageable);
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
        BeverageEntity entity = beverageRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Beverage not found with id: {}", id);
                    return new NoSuchElementException("Beverage not found");
                });

        deleteStoredImage(entity.getImageUrl());
        beverageRepository.delete(entity);
        log.info("Beverage deleted with id: {}", id);
    }

    public String getSignedImageUrl(UUID id) {
        BeverageEntity entity = beverageRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Beverage not found"));

        if (entity.getImageUrl() == null || entity.getImageUrl().isEmpty()) {
            throw new IllegalStateException("Beverage has no image");
        }
        String filePath = entity.getImageUrl();

        if (filePath.contains("/")) {
            String[] parts = filePath.split("/", 2);
            if (parts.length == 2) {
                return storageService.generateSignedUrl(parts[0], parts[1]);
            }
        }
        return storageService.generateSignedUrl(IMAGE_BUCKET, filePath);
    }

    public BeverageDto uploadImage(UUID id, MultipartFile image) {
        BeverageEntity entity = beverageRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Beverage not found"));

        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Image file is required");
        }

        String filePath = id + getImageExtension(image.getOriginalFilename(), image.getContentType());
        String imageUrl = storageService.upload(IMAGE_BUCKET, filePath, image);

        entity.setImageUrl(imageUrl);
        entity.setUpdatedAt(OffsetDateTime.now());

        return beverageMapper.toDto(beverageRepository.save(entity));
    }

    public void deleteImage(UUID id) {
        BeverageEntity entity = beverageRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Beverage not found"));

        deleteStoredImage(entity.getImageUrl());
        entity.setImageUrl(null);
        entity.setUpdatedAt(OffsetDateTime.now());
        beverageRepository.save(entity);
    }

    private void deleteStoredImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return;
        }

        if (imageUrl.contains("/")) {
            String[] parts = imageUrl.split("/", 2);
            if (parts.length == 2) {
                storageService.delete(parts[0], parts[1]);
                return;
            }
        }

        storageService.delete(IMAGE_BUCKET, imageUrl);
    }

    private String getImageExtension(String originalFilename, String contentType) {
        if (originalFilename != null) {
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex >= 0 && dotIndex < originalFilename.length() - 1) {
                return originalFilename.substring(dotIndex).toLowerCase();
            }
        }

        if ("image/png".equalsIgnoreCase(contentType)) {
            return ".png";
        }
        if ("image/webp".equalsIgnoreCase(contentType)) {
            return ".webp";
        }
        return ".jpg";
    }
}
