package com.fuelprice.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fuelprice.config.MimitProperties;
import com.fuelprice.dto.ImportResultResponse;
import com.fuelprice.model.FuelStation;
import com.fuelprice.model.FuelType;
import com.fuelprice.repository.FuelStationRepository;
import com.fuelprice.util.CsvUtils;

@Service
public class MimitFuelImportService {
	private final MimitProperties properties;
	private final FuelStationRepository repository;

	public MimitFuelImportService(MimitProperties properties, FuelStationRepository repository) {
		this.properties = properties;
		this.repository = repository;
	}

	@Scheduled(cron = "0 30 9 * * *")
	@Transactional
	public void scheduledImport() {
		importFromMimit();
	}

	@Transactional
	public ImportResultResponse importFromMimit() {

		Map<String, StationMaster> stations = downloadStations();
		List<PriceRow> prices = downloadPrices();

		repository.deleteAllInBatch();

		int saved = 0;

		List<FuelStation> batch = new ArrayList<>();

		Set<String> seenKeys = new HashSet<>();

		for (PriceRow price : prices) {

			FuelType fuelType = mapFuelType(price.fuelName());

			if (fuelType == null) {
				continue;
			}

			StationMaster master = stations.get(price.stationId());

			if (master == null || master.latitude() == null || master.longitude() == null) {
				continue;
			}

			String uniqueKey = price.stationId() + "|" + fuelType + "|" + price.selfService();

			if (!seenKeys.add(uniqueKey)) {
				continue;
			}

			FuelStation station = new FuelStation();

			station.setExternalId(price.stationId());

			station.setName(firstNotBlank(master.brand(), master.owner(), "Distributore"));

			station.setBrand(master.brand());

			station.setAddress(master.address());

			station.setLatitude(master.latitude());

			station.setLongitude(master.longitude());

			station.setFuelType(fuelType);

			station.setPrice(price.price());

			station.setSelfService(price.selfService());

			station.setPriceUpdatedAt(price.updatedAt());

			batch.add(station);

			saved++;

			if (batch.size() >= 1000) {

				repository.saveAll(batch);

				repository.flush();

				batch.clear();
			}
		}

		if (!batch.isEmpty()) {

			repository.saveAll(batch);

			repository.flush();
		}

		return new ImportResultResponse(stations.size(), prices.size(), saved);
	}

	private Map<String, StationMaster> downloadStations() {
		Map<String, StationMaster> result = new HashMap<>();

		try (BufferedReader reader = new BufferedReader(
//            new InputStreamReader(URI.create(properties.getStationsUrl()).toURL().openStream(), StandardCharsets.UTF_8)

				new InputStreamReader(openStream(properties.getStationsUrl()), StandardCharsets.UTF_8)

		)) {
			String header = reader.readLine();
			header = reader.readLine();
			if (header == null)
				return result;

			List<String> headers = CsvUtils.split(header);
			String line;
			while ((line = reader.readLine()) != null) {
				List<String> cols = CsvUtils.split(line);

				String id = get(cols, headers, "idImpianto", "IdImpianto", "id", "ID");
				String owner = get(cols, headers, "Gestore", "gestore", "NomeGestore", "nomeGestore");
				String brand = get(cols, headers, "Bandiera", "bandiera", "Brand", "brand");
				String address = get(cols, headers, "Indirizzo", "indirizzo");
				String comune = get(cols, headers, "Comune", "comune");
				String provincia = get(cols, headers, "Provincia", "provincia");
				Double latitude = parseDouble(get(cols, headers, "Latitudine", "latitudine", "lat"));
				Double longitude = parseDouble(get(cols, headers, "Longitudine", "longitudine", "lng", "lon"));

				if (isBlank(id))
					continue;

				result.put(id, new StationMaster(id, owner, brand, joinAddress(address, comune, provincia), latitude,
						longitude));
			}
		} catch (Exception e) {
			throw new RuntimeException("Errore durante download/import anagrafica MIMIT", e);
		}

		return result;
	}

	private List<PriceRow> downloadPrices() {
		List<PriceRow> result = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(

				new InputStreamReader(openStream(properties.getPricesUrl()), StandardCharsets.UTF_8))) {
			String header = reader.readLine();
			header = reader.readLine();
			if (header == null)
				return result;

			List<String> headers = CsvUtils.split(header);
			String line;
			while ((line = reader.readLine()) != null) {
				List<String> cols = CsvUtils.split(line);

				String id = get(cols, headers, "idImpianto", "IdImpianto", "id", "ID");
				String fuelName = get(cols, headers, "descCarburante", "DescCarburante", "carburante", "Carburante");
				Double price = parseDouble(get(cols, headers, "prezzo", "Prezzo"));
				String selfValue = get(cols, headers, "isSelf", "self", "Self", "tipo");
				LocalDateTime updatedAt = parseDateTime(
						get(cols, headers, "dtComu", "dataComunicazione", "DataComunicazione"));

				if (isBlank(id) || isBlank(fuelName) || price == null)
					continue;

				result.add(new PriceRow(id, fuelName, price, parseSelfService(selfValue), updatedAt));
			}
		} catch (Exception e) {
			throw new RuntimeException("Errore durante download/import prezzi MIMIT", e);
		}

		return result;
	}

	private FuelType mapFuelType(String value) {
		if (value == null)
			return null;
		String v = value.toLowerCase(Locale.ITALY);

		if (v.contains("benzina"))
			return FuelType.BENZINA;
		if (v.contains("gasolio") || v.contains("diesel"))
			return FuelType.DIESEL;

		return null;
	}

	private Boolean parseSelfService(String value) {
		if (value == null)
			return null;
		String v = value.trim().toLowerCase(Locale.ITALY);

		if (v.equals("1") || v.equals("true") || v.contains("self"))
			return true;
		if (v.equals("0") || v.equals("false") || v.contains("serv"))
			return false;

		return null;
	}

	private String get(List<String> cols, List<String> headers, String... names) {
		for (String name : names) {
			int idx = indexOf(headers, name);
			if (idx >= 0 && idx < cols.size()) {
				String value = cols.get(idx);
				if (!isBlank(value))
					return value;
			}
		}
		return null;
	}

	private int indexOf(List<String> headers, String name) {
		for (int i = 0; i < headers.size(); i++) {
			if (headers.get(i).trim().equalsIgnoreCase(name))
				return i;
		}
		return -1;
	}

	private Double parseDouble(String value) {
		if (isBlank(value))
			return null;
		try {
			return Double.parseDouble(value.trim().replace(",", "."));
		} catch (Exception e) {
			return null;
		}
	}

	private LocalDateTime parseDateTime(String value) {
		if (isBlank(value))
			return LocalDateTime.now();

		List<DateTimeFormatter> formatters = List.of(DateTimeFormatter.ISO_LOCAL_DATE_TIME,
				DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"),
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		for (DateTimeFormatter formatter : formatters) {
			try {
				return LocalDateTime.parse(value.trim(), formatter);
			} catch (Exception ignored) {
			}
		}

		return LocalDateTime.now();
	}

	private String joinAddress(String address, String comune, String provincia) {
		return String.join(", ",
				Arrays.stream(new String[] { address, comune, provincia }).filter(s -> !isBlank(s)).toList());
	}

	private String firstNotBlank(String... values) {
		for (String value : values) {
			if (!isBlank(value))
				return value;
		}
		return null;
	}

	private boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}

	private record StationMaster(String stationId, String owner, String brand, String address, Double latitude,
			Double longitude) {
	}

	private record PriceRow(String stationId, String fuelName, Double price, Boolean selfService,
			LocalDateTime updatedAt) {
	}

	private InputStream openStream(String path) throws Exception {

		if (path.startsWith("file:")) {
			return Files.newInputStream(Path.of(path.replace("file:", "")));
		}

		return URI.create(path).toURL().openStream();
	}
}
