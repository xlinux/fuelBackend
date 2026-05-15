package com.fuelprice.service;

import com.fuelprice.dto.FuelEntryRequest;
import com.fuelprice.dto.FuelEntryResponse;
import com.fuelprice.model.UserFuelEntry;
import com.fuelprice.repository.UserCarRepository;
import com.fuelprice.repository.UserFuelEntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserFuelEntryService {
    private final UserFuelEntryRepository repository;
    private final UserCarRepository carRepository;
    private final DeviceService deviceService;

    public UserFuelEntryService(UserFuelEntryRepository repository,
                                UserCarRepository carRepository,
                                DeviceService deviceService) {
        this.repository = repository;
        this.carRepository = carRepository;
        this.deviceService = deviceService;
    }

    public List<FuelEntryResponse> findEntries(String deviceId) {
        String cleanDeviceId = deviceService.requireAndEnsureDevice(deviceId);
        return repository.findByDeviceIdOrderByEntryDateDesc(cleanDeviceId)
                .stream().map(this::toResponse).toList();
    }

    @Transactional
    public FuelEntryResponse createEntry(String deviceId, FuelEntryRequest request) {
        String cleanDeviceId = deviceService.requireAndEnsureDevice(deviceId);

        UserFuelEntry entry = new UserFuelEntry();
        entry.setDeviceId(cleanDeviceId);
        entry.setCarId(request.carId());
        entry.setCarName(request.carName());
        entry.setFuelType(request.fuelType());
        entry.setAmount(request.amount());
        entry.setOdometerKm(request.odometerKm());
        entry.setFuelPrice(request.fuelPrice());
        entry.setGpsEstimatedKm(request.gpsEstimatedKm());
        entry.setLatitude(request.latitude());
        entry.setLongitude(request.longitude());
        entry.setAddress(request.address());
        entry.setStationName(request.stationName());
        entry.setEntryDate(request.entryDate() == null ? LocalDateTime.now() : request.entryDate());
        entry.setCreatedAt(LocalDateTime.now());

        if (request.carId() != null) {
            carRepository.findById(request.carId())
                    .filter(c -> c.getDeviceId().equals(cleanDeviceId))
                    .ifPresent(car -> {
                        entry.setCarName(car.getName());
                        entry.setFuelType(car.getFuelType());
                    });
        }

        return toResponse(repository.save(entry));
    }

    @Transactional
    public void deleteEntry(String deviceId, UUID id) {
        String cleanDeviceId = deviceService.requireAndEnsureDevice(deviceId);
        repository.deleteByDeviceIdAndId(cleanDeviceId, id);
    }

    private FuelEntryResponse toResponse(UserFuelEntry entry) {
        return new FuelEntryResponse(
                entry.getId(), entry.getCarId(), entry.getCarName(), entry.getFuelType(),
                entry.getAmount(), entry.getOdometerKm(), entry.getFuelPrice(), entry.getGpsEstimatedKm(),
                entry.getLatitude(), entry.getLongitude(), entry.getAddress(), entry.getStationName(),
                entry.getEntryDate(), entry.getCreatedAt()
        );
    }
}
