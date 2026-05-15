package com.fuelprice.controller;

import com.fuelprice.dto.FuelEntryRequest;
import com.fuelprice.dto.FuelEntryResponse;
import com.fuelprice.service.UserFuelEntryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/me/fuel-entries")
@CrossOrigin
public class UserFuelEntryController {
    private final UserFuelEntryService service;

    public UserFuelEntryController(UserFuelEntryService service) {
        this.service = service;
    }

    @GetMapping
    public List<FuelEntryResponse> findEntries(@RequestHeader("X-Device-Id") String deviceId) {
        return service.findEntries(deviceId);
    }

    @PostMapping
    public FuelEntryResponse createEntry(@RequestHeader("X-Device-Id") String deviceId,
                                         @RequestBody FuelEntryRequest request) {
        return service.createEntry(deviceId, request);
    }

    @DeleteMapping("/{id}")
    public void deleteEntry(@RequestHeader("X-Device-Id") String deviceId,
                            @PathVariable UUID id) {
        service.deleteEntry(deviceId, id);
    }
}
