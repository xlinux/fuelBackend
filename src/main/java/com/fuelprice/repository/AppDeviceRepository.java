package com.fuelprice.repository;

import com.fuelprice.model.AppDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface AppDeviceRepository extends JpaRepository<AppDevice, UUID> {
    Optional<AppDevice> findByDeviceId(String deviceId);
}
