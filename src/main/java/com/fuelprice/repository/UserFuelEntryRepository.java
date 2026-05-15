package com.fuelprice.repository;

import com.fuelprice.model.UserFuelEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface UserFuelEntryRepository extends JpaRepository<UserFuelEntry, UUID> {
    List<UserFuelEntry> findByDeviceIdOrderByEntryDateDesc(String deviceId);
    void deleteByDeviceIdAndId(String deviceId, UUID id);
    void deleteByDeviceIdAndCarId(String deviceId, UUID carId);
}
