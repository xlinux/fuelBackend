package com.fuelprice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "app_device", uniqueConstraints = {
        @UniqueConstraint(name = "uk_app_device_device_id", columnNames = "deviceId")
})
public class AppDevice {
    @Id @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String deviceId;

    private UUID userId;
    private LocalDateTime createdAt;

    protected AppDevice() {}

    public AppDevice(String deviceId) {
        this.deviceId = deviceId;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public String getDeviceId() { return deviceId; }
    public UUID getUserId() { return userId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setUserId(UUID userId) { this.userId = userId; }
}
