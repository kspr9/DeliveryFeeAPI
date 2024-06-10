# Weather Data API Documentation

## Base URL
/api/weather


## Endpoints

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

### Import Weather Data

URL: /api/weather/import
Method: GET
Success Response:
Code: 200 OK
Example: