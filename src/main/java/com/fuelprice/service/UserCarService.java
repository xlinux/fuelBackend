package com.fuelprice.service;

import com.fuelprice.dto.CarRequest;
import com.fuelprice.dto.CarResponse;
import com.fuelprice.model.UserCar;
import com.fuelprice.repository.UserCarRepository;
import com.fuelprice.repository.UserFuelEntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserCarService {
    private final UserCarRepository carRepository;
    private final UserFuelEntryRepository fuelEntryRepository;
    private final DeviceService deviceService;

    public UserCarService(UserCarRepository carRepository,
                          UserFuelEntryRepository fuelEntryRepository,
                          DeviceService deviceService) {
        this.carRepository = carRepository;
        this.fuelEntryRepository = fuelEntryRepository;
        this.deviceService = deviceService;
    }

    public List<CarResponse> findCars(String deviceId) {
        String cleanDeviceId = deviceService.requireAndEnsureDevice(deviceId);
        return carRepository.findByDeviceIdOrderByCreatedAtAsc(cleanDeviceId)
                .stream().map(this::toResponse).toList();
    }

    @Transactional
    public CarResponse createCar(String deviceId, CarRequest request) {
        String cleanDeviceId = deviceService.requireAndEnsureDevice(deviceId);

        boolean makeDefault = Boolean.TRUE.equals(request.defaultCar())
                || carRepository.findByDeviceIdOrderByCreatedAtAsc(cleanDeviceId).isEmpty();

        if (makeDefault) {
            clearDefault(cleanDeviceId);
        }

        UserCar car = new UserCar(cleanDeviceId, request.name().trim(), request.fuelType(), makeDefault);
        return toResponse(carRepository.save(car));
    }

    @Transactional
    public CarResponse updateCar(String deviceId, UUID carId, CarRequest request) {
        String cleanDeviceId = deviceService.requireAndEnsureDevice(deviceId);

        UserCar car = carRepository.findById(carId)
                .filter(c -> c.getDeviceId().equals(cleanDeviceId))
                .orElseThrow(() -> new IllegalArgumentException("Auto non trovata"));

        car.setName(request.name().trim());
        car.setFuelType(request.fuelType());

        if (Boolean.TRUE.equals(request.defaultCar())) {
            clearDefault(cleanDeviceId);
            car.setDefaultCar(true);
        }

        return toResponse(carRepository.save(car));
    }

    @Transactional
    public void deleteCar(String deviceId, UUID carId) {
        String cleanDeviceId = deviceService.requireAndEnsureDevice(deviceId);
        fuelEntryRepository.deleteByDeviceIdAndCarId(cleanDeviceId, carId);
        carRepository.deleteByDeviceIdAndId(cleanDeviceId, carId);
    }

    @Transactional
    public void setDefaultCar(String deviceId, UUID carId) {
        String cleanDeviceId = deviceService.requireAndEnsureDevice(deviceId);

        UserCar car = carRepository.findById(carId)
                .filter(c -> c.getDeviceId().equals(cleanDeviceId))
                .orElseThrow(() -> new IllegalArgumentException("Auto non trovata"));

        clearDefault(cleanDeviceId);
        car.setDefaultCar(true);
        carRepository.save(car);
    }

    private void clearDefault(String deviceId) {
        List<UserCar> cars = carRepository.findByDeviceIdOrderByCreatedAtAsc(deviceId);
        cars.forEach(c -> c.setDefaultCar(false));
        carRepository.saveAll(cars);
    }

    private CarResponse toResponse(UserCar car) {
        return new CarResponse(car.getId(), car.getName(), car.getFuelType(), car.getDefaultCar(), car.getCreatedAt());
    }
}
