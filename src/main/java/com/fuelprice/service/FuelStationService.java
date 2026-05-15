package com.fuelprice.service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.fuelprice.dto.BestStationResponse;
import com.fuelprice.dto.NearbyStationResponse;
import com.fuelprice.model.FuelStation;
import com.fuelprice.model.FuelType;
import com.fuelprice.repository.FuelStationRepository;

@Service
public class FuelStationService {
	private final FuelStationRepository repository;

	public FuelStationService(FuelStationRepository repository) {
		this.repository = repository;
	}

	public List<NearbyStationResponse> findNearby(double lat, double lng, FuelType fuelType, double radiusMeters,
			int limit) {
		return repository.findByFuelType(fuelType).stream()
				.filter(s -> s.getLatitude() != null && s.getLongitude() != null && s.getPrice() != null)
				.map(s -> toResponse(s, lat, lng)).filter(s -> s.distanceMeters() <= radiusMeters).sorted(Comparator
						.comparing(NearbyStationResponse::distanceMeters).thenComparing(NearbyStationResponse::price))
				.limit(limit).toList();
	}

	private NearbyStationResponse toResponse(FuelStation station, double lat, double lng) {
		double distance = DistanceUtils.distanceMeters(lat, lng, station.getLatitude(), station.getLongitude());

		return new NearbyStationResponse(station.getId(), station.getExternalId(), station.getName(),
				station.getBrand(), station.getAddress(), station.getLatitude(), station.getLongitude(), distance,
				station.getFuelType(), station.getPrice(), station.getSelfService(), station.getPriceUpdatedAt());
	}

	public BestStationResponse findBestStation(double lat, double lng, FuelType fuelType, double liters,
			double carKmPerLiter, double radiusMeters, boolean roundTrip) {
		return repository.findByFuelType(fuelType).stream().filter(s -> s.getLatitude() != null)
				.filter(s -> s.getLongitude() != null).filter(s -> s.getPrice() != null).map(station -> {
					double distanceMeters = DistanceUtils.distanceMeters(lat, lng, station.getLatitude(),
							station.getLongitude());

					double distanceKm = distanceMeters / 1000.0;
					double effectiveDistanceKm = roundTrip ? distanceKm * 2 : distanceKm;

					double fuelCost = liters * station.getPrice();
					double travelCost = (effectiveDistanceKm / carKmPerLiter) * station.getPrice();
					double estimatedTotalCost = fuelCost + travelCost;

					return new BestStationResponse(station.getId(), station.getExternalId(), station.getName(),
							station.getBrand(), station.getAddress(), station.getLatitude(), station.getLongitude(),
							distanceMeters, station.getFuelType(), station.getPrice(), station.getSelfService(),
							station.getPriceUpdatedAt(), liters, fuelCost, travelCost, estimatedTotalCost);
				}).filter(s -> s.distanceMeters() <= radiusMeters)
				.min(Comparator.comparing(BestStationResponse::estimatedTotalCost))
				.orElseThrow(() -> new RuntimeException("Nessun distributore trovato"));
	}

	public List<BestStationResponse> findBestStations(
	        double lat,
	        double lng,
	        FuelType fuelType,
	        double liters,
	        double carKmPerLiter,
	        double radiusMeters,
	        int limit
	) {
	    Set<String> seenStations = new HashSet<>();

	    return repository.findByFuelType(fuelType)
	            .stream()
	            .filter(s -> s.getLatitude() != null)
	            .filter(s -> s.getLongitude() != null)
	            .filter(s -> s.getPrice() != null)
	            .map(station -> {
	                double distanceMeters = DistanceUtils.distanceMeters(
	                        lat,
	                        lng,
	                        station.getLatitude(),
	                        station.getLongitude()
	                );

	                double distanceKm = distanceMeters / 1000.0;
	                double travelCost = ((distanceKm * 2) / carKmPerLiter) * station.getPrice();
	                double fuelCost = liters * station.getPrice();
	                double estimatedTotalCost = fuelCost + travelCost;

	                return new BestStationResponse(
	                        station.getId(),
	                        station.getExternalId(),
	                        station.getName(),
	                        station.getBrand(),
	                        station.getAddress(),
	                        station.getLatitude(),
	                        station.getLongitude(),
	                        distanceMeters,
	                        station.getFuelType(),
	                        station.getPrice(),
	                        station.getSelfService(),
	                        station.getPriceUpdatedAt(),
	                        liters,
	                        fuelCost,
	                        travelCost,
	                        estimatedTotalCost
	                );
	            })
	            .filter(s -> s.distanceMeters() <= radiusMeters)
	            .sorted(Comparator.comparing(BestStationResponse::estimatedTotalCost))
	            .filter(s -> seenStations.add(s.externalId()))
	            .limit(limit)
	            .toList();
	}
}
