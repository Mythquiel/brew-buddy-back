package com.brewbuddy.app;

import com.brewbuddy.domain.BrewLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface BrewLogRepository extends JpaRepository<BrewLogEntity, UUID>, JpaSpecificationExecutor<BrewLogEntity> {
}
