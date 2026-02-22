package com.brewbuddy.app;

import com.brewbuddy.domain.BeverageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface BeverageRepository extends JpaRepository<BeverageEntity, UUID>, JpaSpecificationExecutor<BeverageEntity> {
}
