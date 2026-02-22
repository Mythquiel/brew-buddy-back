package com.brewbuddy.api;

import com.brewbuddy.api.dto.BeverageQuantityCreateDto;
import com.brewbuddy.api.dto.BeverageQuantityDto;
import com.brewbuddy.api.dto.BeverageQuantityUpdateDto;
import com.brewbuddy.app.BeverageQuantityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/beverageQuantity")
@RequiredArgsConstructor
public class BeverageQuantityController {

    private final BeverageQuantityService service;

    @GetMapping("/{id}")
    public BeverageQuantityDto get(@PathVariable UUID id) {
        return service.get(id);
    }

    @PostMapping
    public ResponseEntity<BeverageQuantityDto> create(@Valid @RequestBody BeverageQuantityCreateDto in) {
        BeverageQuantityDto out = service.create(in);
        return ResponseEntity.created(
                URI.create("/api/v1/beverageQuantity/" + out.getBeverageId())
        ).body(out);
    }

    @PatchMapping("/{id}")
    public BeverageQuantityDto update(@PathVariable UUID id, @Valid @RequestBody BeverageQuantityUpdateDto in) {
        return service.update(id, in);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
