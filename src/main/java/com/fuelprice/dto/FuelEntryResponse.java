package com.fuelprice.dto;

import com.fuelprice.model.FuelType;
import java.time.LocalDateTime;
import java.util.UUID;

public record FuelEntryResponse(
        UUID id,
        UUID carId,
        String carName,
        FuelType fuelType,
        Double amount,
        Double odometerKm,
        Double fuelPrice,
        Double gpsEstimatedKm,
        Double latitude,
        Double longitude,
        String address,
        String stationName,
        LocalDateTime entryDate,
        LocalDateTime createdAt
) {}
