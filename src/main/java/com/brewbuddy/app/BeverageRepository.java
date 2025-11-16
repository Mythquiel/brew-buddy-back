package com.brewbuddy.app;

import com.brewbuddy.domain.BeverageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeverageRepository extends JpaRepository<BeverageEntity, UUID> {
}
