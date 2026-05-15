package com.fuelprice.repository;

import com.fuelprice.model.UserCar;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface UserCarRepository extends JpaRepository<UserCar, UUID> {
    List<UserCar> findByDeviceIdOrderByCreatedAtAsc(String deviceId);
    void deleteByDeviceIdAndId(String deviceId, UUID id);
}
