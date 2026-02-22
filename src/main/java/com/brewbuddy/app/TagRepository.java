package com.brewbuddy.app;

import com.brewbuddy.domain.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface TagRepository extends JpaRepository<TagEntity, UUID>, JpaSpecificationExecutor<TagEntity> {
}
