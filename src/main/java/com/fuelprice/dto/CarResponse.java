package com.fuelprice.dto;

import com.fuelprice.model.FuelType;
import java.time.LocalDateTime;
import java.util.UUID;

public record CarResponse(
        UUID id,
        String name,
        FuelType fuelType,
        Boolean defaultCar,
        LocalDateTime createdAt
) {}
