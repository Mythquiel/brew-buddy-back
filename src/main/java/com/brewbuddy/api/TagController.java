package com.brewbuddy.api;

import com.brewbuddy.api.dto.TagCreateDto;
import com.brewbuddy.api.dto.TagDto;
import com.brewbuddy.api.dto.TagUpdateDto;
import com.brewbuddy.app.TagService;
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
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService service;

    @GetMapping
    public Page<TagDto> list(
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return service.list(pageable);
    }

    @GetMapping("/{id}")
    public TagDto get(@PathVariable UUID id) {
        return service.get(id);
    }

    @PostMapping
    public ResponseEntity<TagDto> create(@RequestBody TagCreateDto in) {
        TagDto out = service.create(in);
        return ResponseEntity.created(
                URI.create("/api/v1/Tags/" + out.getId())
        ).body(out);
    }

    @PatchMapping("/{id}")
    public TagDto update(@PathVariable UUID id, @RequestBody TagUpdateDto in) {
        return service.update(id, in);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
