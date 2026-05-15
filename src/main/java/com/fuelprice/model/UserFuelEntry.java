package com.fuelprice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_fuel_entry")
public class UserFuelEntry {
    @Id @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String deviceId;

    private UUID userId;
    private UUID carId;
    private String carName;

    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Double odometerKm;

    private Double fuelPrice;
    private Double gpsEstimatedKm;
    private Double latitude;
    private Double longitude;

    @Column(length = 1000)
    private String address;

    private String stationName;
    private LocalDateTime entryDate;
    private LocalDateTime createdAt;

    public UUID getId() { return id; }
    public String getDeviceId() { return deviceId; }
    public UUID getUserId() { return userId; }
    public UUID getCarId() { return carId; }
    public String getCarName() { return carName; }
    public FuelType getFuelType() { return fuelType; }
    public Double getAmount() { return amount; }
    public Double getOdometerKm() { return odometerKm; }
    public Double getFuelPrice() { return fuelPrice; }
    public Double getGpsEstimatedKm() { return gpsEstimatedKm; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getAddress() { return address; }
    public String getStationName() { return stationName; }
    public LocalDateTime getEntryDate() { return entryDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public void setCarId(UUID carId) { this.carId = carId; }
    public void setCarName(String carName) { this.carName = carName; }
    public void setFuelType(FuelType fuelType) { this.fuelType = fuelType; }
    public void setAmount(Double amount) { this.amount = amount; }
    public void setOdometerKm(Double odometerKm) { this.odometerKm = odometerKm; }
    public void setFuelPrice(Double fuelPrice) { this.fuelPrice = fuelPrice; }
    public void setGpsEstimatedKm(Double gpsEstimatedKm) { this.gpsEstimatedKm = gpsEstimatedKm; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public void setAddress(String address) { this.address = address; }
    public void setStationName(String stationName) { this.stationName = stationName; }
    public void setEntryDate(LocalDateTime entryDate) { this.entryDate = entryDate; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
