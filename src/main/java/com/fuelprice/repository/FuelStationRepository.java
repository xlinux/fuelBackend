package com.fuelprice.repository;

import com.fuelprice.model.FuelStation;
import com.fuelprice.model.FuelType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FuelStationRepository extends JpaRepository<FuelStation, UUID> {
    List<FuelStation> findByFuelType(FuelType fuelType);
    Optional<FuelStation> findByExternalIdAndFuelType(String externalId, FuelType fuelType);
}
