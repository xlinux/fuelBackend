package com.fuelprice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fuelprice.dto.BestStationResponse;
import com.fuelprice.dto.NearbyStationResponse;
import com.fuelprice.model.FuelType;
import com.fuelprice.service.FuelStationService;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/stations")
@CrossOrigin
public class FuelStationController {
	private final FuelStationService fuelStationService;

	public FuelStationController(FuelStationService fuelStationService) {
		this.fuelStationService = fuelStationService;
	}

	@GetMapping("/nearby")
	public List<NearbyStationResponse> nearby(@RequestParam double lat, @RequestParam double lng,
			@RequestParam FuelType fuelType,
			@RequestParam(defaultValue = "3000") @Min(100) @Max(20000) double radiusMeters,
			@RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit) {
		return fuelStationService.findNearby(lat, lng, fuelType, radiusMeters, limit);
	}

	@GetMapping("/best")
	public BestStationResponse best(@RequestParam double lat, @RequestParam double lng, @RequestParam FuelType fuelType,
			@RequestParam(defaultValue = "30") @Min(1) @Max(120) double liters,
			@RequestParam(defaultValue = "15") @Min(3) @Max(40) double carKmPerLiter,
			@RequestParam(defaultValue = "10000") @Min(100) @Max(50000) double radiusMeters,
			@RequestParam(defaultValue = "true") boolean roundTrip) {
		return fuelStationService.findBestStation(lat, lng, fuelType, liters, carKmPerLiter, radiusMeters, roundTrip);
	}

	@GetMapping("/best-options")
	public List<BestStationResponse> bestOptions(@RequestParam double lat, @RequestParam double lng,
			@RequestParam FuelType fuelType, @RequestParam(defaultValue = "30") double liters,
			@RequestParam(defaultValue = "15") double carKmPerLiter,
			@RequestParam(defaultValue = "10000") double radiusMeters, @RequestParam(defaultValue = "2") int limit) {
		return fuelStationService.findBestStations(lat, lng, fuelType, liters, carKmPerLiter, radiusMeters, limit);
	}
}
