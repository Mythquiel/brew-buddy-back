package com.brewbuddy.api;

import com.brewbuddy.api.dto.BeverageCreateDto;
import com.brewbuddy.api.dto.BeverageDto;
import com.brewbuddy.api.dto.BeverageUpdateDto;
import com.brewbuddy.app.BeverageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/beverages")
@RequiredArgsConstructor
public class BeverageController {

    private final BeverageService service;

    @GetMapping
    public Page<BeverageDto> list(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String nameContains,
            @RequestParam(required = false) String brand,
            @PageableDefault(size = 20, sort = "name") Pageable pageable
    ) {
        return service.list(type, nameContains, brand, pageable);
    }

    @GetMapping("/{id}")
    public BeverageDto get(@PathVariable UUID id) {
        return service.get(id);
    }

    @PostMapping
    public ResponseEntity<BeverageDto> create(@RequestBody BeverageCreateDto in) {
        BeverageDto out = service.create(in);
        return ResponseEntity.created(
                URI.create("/api/v1/beverages/" + out.getId())
        ).body(out);
    }

    @PatchMapping("/{id}")
    public BeverageDto update(@PathVariable UUID id, @RequestBody BeverageUpdateDto in) {
        return service.update(id, in);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/image-url")
    public ResponseEntity<String> getImageUrl(@PathVariable UUID id) {
        String signedUrl = service.getSignedImageUrl(id);
        return ResponseEntity.ok(signedUrl);
    }
}
