package com.brewbuddy.app;

import com.brewbuddy.domain.BrewLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BrewLogRepository extends JpaRepository<BrewLogEntity, UUID> {
}
