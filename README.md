# Fuel Price Backend

Backend Spring Boot che importa dati MIMIT carburanti e restituisce i benzinai vicini con prezzo.

## Avvio

```bash
mvn spring-boot:run
```

## Import dati MIMIT

```bash
curl -X POST http://localhost:8080/api/import/mimit
```

## Ricerca benzinai vicini

```bash
curl "http://localhost:8080/api/stations/nearby?lat=41.9028&lng=12.4964&fuelType=BENZINA&radiusMeters=3000"
```

Fuel type:
- BENZINA
- DIESEL

## H2 Console

- URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:file:./data/fuelprice
- User: sa
- Password: vuota
