package com.brewbuddy.app;

import com.brewbuddy.domain.BeverageQuantityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeverageQuantityRepository extends JpaRepository<BeverageQuantityEntity, UUID> {
}
