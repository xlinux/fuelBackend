package com.fuelprice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_car")
public class UserCar {
    @Id @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String deviceId;

    private UUID userId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FuelType fuelType;

    private Boolean defaultCar;
    private LocalDateTime createdAt;

    protected UserCar() {}

    public UserCar(String deviceId, String name, FuelType fuelType, Boolean defaultCar) {
        this.deviceId = deviceId;
        this.name = name;
        this.fuelType = fuelType;
        this.defaultCar = defaultCar;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public String getDeviceId() { return deviceId; }
    public UUID getUserId() { return userId; }
    public String getName() { return name; }
    public FuelType getFuelType() { return fuelType; }
    public Boolean getDefaultCar() { return defaultCar; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public void setName(String name) { this.name = name; }
    public void setFuelType(FuelType fuelType) { this.fuelType = fuelType; }
    public void setDefaultCar(Boolean defaultCar) { this.defaultCar = defaultCar; }
}
