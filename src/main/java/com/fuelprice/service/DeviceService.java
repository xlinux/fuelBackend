package com.fuelprice.service;

import com.fuelprice.model.AppDevice;
import com.fuelprice.repository.AppDeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeviceService {
    private final AppDeviceRepository repository;

    public DeviceService(AppDeviceRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public String requireAndEnsureDevice(String deviceId) {
        if (deviceId == null || deviceId.trim().isEmpty()) {
            throw new IllegalArgumentException("Header X-Device-Id mancante");
        }

        String clean = deviceId.trim();
        repository.findByDeviceId(clean).orElseGet(() -> repository.save(new AppDevice(clean)));
        return clean;
    }
}
