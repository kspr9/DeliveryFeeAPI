# Weather Data API Documentation

## Base URL
/api/weather


## Endpoints

### Import Weather Data

**GET** `/import`

Manually triggers the import of weather data.

- **Responses:**
    - `200 OK`: Successful trigger of weather data import.

**Example Request:**
```bash
curl -X GET "http://<base-url>/api/weather/import"
```
---

### Retrieve Weather Data by ID

**GET** `/{id}`

Retrieves a WeatherData object by its ID.

- **Parameters:**
  - `id` (Long, required): The ID of the WeatherData object to retrieve.

- **Responses:**
  - `200 OK`: Returns the WeatherData object with the specified ID.
  - `404 NOT FOUND`: Returns an error message if the WeatherData object is not found.

**Example Request:**
```bash
curl -X GET "http://<base-url>/api/weather/1"
```

