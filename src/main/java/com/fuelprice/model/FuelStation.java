package com.fuelprice.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "fuel_station",
    indexes = {
        @Index(
            name = "idx_fuel_station_external_fuel_self",
            columnList = "externalId,fuelType,selfService"
        ),
        @Index(
            name = "idx_fuel_station_fuel",
            columnList = "fuelType"
        ),
        @Index(
            name = "idx_fuel_station_lat_lng",
            columnList = "latitude,longitude"
        )
    },
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_station_external_fuel_self",
            columnNames = {
                "externalId",
                "fuelType",
                "selfService"
            }
        )
    }
)
public class FuelStation {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String externalId;

    private String name;

    private String brand;

    @Column(length = 1000)
    private String address;

    private Double latitude;

    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FuelType fuelType;

    private Double price;

    private Boolean selfService;

    private LocalDateTime priceUpdatedAt;

    public UUID getId() {
        return id;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getAddress() {
        return address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public Double getPrice() {
        return price;
    }

    public Boolean getSelfService() {
        return selfService;
    }

    public LocalDateTime getPriceUpdatedAt() {
        return priceUpdatedAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setSelfService(Boolean selfService) {
        this.selfService = selfService;
    }

    public void setPriceUpdatedAt(LocalDateTime priceUpdatedAt) {
        this.priceUpdatedAt = priceUpdatedAt;
    }
}