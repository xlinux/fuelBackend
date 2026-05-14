package com.fuelprice.dto;

import com.fuelprice.model.FuelType;
import java.time.LocalDateTime;
import java.util.UUID;

public record NearbyStationResponse(
    UUID id,
    String externalId,
    String name,
    String brand,
    String address,
    Double latitude,
    Double longitude,
    Double distanceMeters,
    FuelType fuelType,
    Double price,
    Boolean selfService,
    LocalDateTime priceUpdatedAt
) {}
