package com.fuelprice.dto;

import com.fuelprice.model.FuelType;

public record CarRequest(
        String name,
        FuelType fuelType,
        Boolean defaultCar
) {}
