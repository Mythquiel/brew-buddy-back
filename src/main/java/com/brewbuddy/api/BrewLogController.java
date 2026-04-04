package com.brewbuddy.api;

import com.brewbuddy.api.dto.BrewLogCreateDto;
import com.brewbuddy.api.dto.BrewLogDto;
import com.brewbuddy.api.dto.BrewLogUpdateDto;
import com.brewbuddy.app.BrewLogService;
import jakarta.validation.Valid;
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
import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/brewLog")
@RequiredArgsConstructor
public class BrewLogController {

    private final BrewLogService service;

    @GetMapping
    public Page<BrewLogDto> list(
            @RequestParam(required = false) UUID beverageId,
            @RequestParam(required = false) OffsetDateTime brewedAfter,
            @RequestParam(required = false) OffsetDateTime brewedBefore,
            @RequestParam(required = false) UUID userId,
            @PageableDefault(size = 20, sort = "brewedAt") Pageable pageable
    ) {
        return service.list(beverageId, brewedAfter, brewedBefore, userId, pageable);
    }

    @GetMapping("/{id}")
    public BrewLogDto get(@PathVariable UUID id) {
        return service.get(id);
    }

    @PostMapping
    public ResponseEntity<BrewLogDto> create(@Valid @RequestBody BrewLogCreateDto in) {
        BrewLogDto out = service.create(in);
        return ResponseEntity.created(
                URI.create("/api/v1/brewLog/" + out.getId())
        ).body(out);
    }

    @PatchMapping("/{id}")
    public BrewLogDto update(@PathVariable UUID id, @Valid @RequestBody BrewLogUpdateDto in) {
        return service.update(id, in);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
