package com.fuelprice.controller;

import com.fuelprice.dto.CarRequest;
import com.fuelprice.dto.CarResponse;
import com.fuelprice.service.UserCarService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/me/cars")
@CrossOrigin
public class UserCarController {
    private final UserCarService service;

    public UserCarController(UserCarService service) {
        this.service = service;
    }

    @GetMapping
    public List<CarResponse> findCars(@RequestHeader("X-Device-Id") String deviceId) {
        return service.findCars(deviceId);
    }

    @PostMapping
    public CarResponse createCar(@RequestHeader("X-Device-Id") String deviceId,
                                 @RequestBody CarRequest request) {
        return service.createCar(deviceId, request);
    }

    @PutMapping("/{id}")
    public CarResponse updateCar(@RequestHeader("X-Device-Id") String deviceId,
                                 @PathVariable UUID id,
                                 @RequestBody CarRequest request) {
        return service.updateCar(deviceId, id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteCar(@RequestHeader("X-Device-Id") String deviceId,
                          @PathVariable UUID id) {
        service.deleteCar(deviceId, id);
    }

    @PutMapping("/{id}/default")
    public void setDefaultCar(@RequestHeader("X-Device-Id") String deviceId,
                              @PathVariable UUID id) {
        service.setDefaultCar(deviceId, id);
    }
}
