# Delivery Fee API Documentation

## Base URL
/api/data

## Endpoints

### Calculate Delivery Fee

**GET** `/calculateDeliveryFee`

Calculate the delivery fee based on city, vehicle type, and optionally a specific date and time.

- **Parameters:**
  - `city` (String, required): The name of the city.
  - `vehicleType` (String, required): The type of vehicle.
  - `dateTime` (LocalDateTime, optional): The date and time in ISO format (e.g., 2023-06-01T15:00:00).

- **Responses:**
  - `200 OK`: Returns the total delivery fee as a float.
  - `403 FORBIDDEN`: Returns an error message "Usage of selected vehicle type is forbidden" if usage of selected vehicle is forbidden.


## Adding new business rules and extra fee rules

### Add Regional Base Fee

**POST** `/regionalBaseFee`

Add a new regional base fee rule to the database.

- **Request Body:**
  - `RegionalBaseFee` (JSON, required): The regional base fee object containing:
    City city (city_id)
    VehicleType vehicleType (vehicleType_id)
    Float baseFee
    LocalDateTime effectiveDate
    Boolean isActive


- **Responses:**
  - `201 CREATED`: Returns the saved regional base fee object.
  - `400 BAD REQUEST`: Returns an error message if the regional base fee could not be saved.

### Add Air Temperature Extra Fee

**POST** `/airTemperatureExtraFee`

Add a new air temperature extra fee to the database.

- **Request Body:**
  - `AirTemperatureExtraFee` (JSON, required): The air temperature extra fee object containing 
    Float minTemp
    Float maxTemp
    Set<VehicleType> applicableVehicles (vehicleType_id)
    Float extraFee
    LocalDateTime effectiveDate
    Boolean isActive

- **Responses:**
  - `201 CREATED`: Returns the saved air temperature extra fee object.
  - `400 BAD REQUEST`: Returns an error message if the air temperature extra fee could not be saved.

### Add Wind Speed Extra Fee

**POST** `/windSpeedExtraFee`

Add a new wind speed extra fee to the database.

- **Request Body:**
  - `WindSpeedExtraFee` (JSON, required): The wind speed extra fee object containing 
    Float minSpeed
    Float maxSpeed
    Float extraFee
    Boolean forbidden
    LocalDateTime effectiveDate
    Boolean isActive
    Set<VehicleType> applicableVehicles (vehicleType_id)

- **Responses:**
  - `201 CREATED`: Returns the saved wind speed extra fee object.
  - `400 BAD REQUEST`: Returns an error message if the wind speed extra fee could not be saved.

### Add Weather Phenomenon Extra Fee

**POST** `/weatherPhenomenonExtraFee`

Add a new weather phenomenon extra fee to the database.

- **Request Body:**
  - `WeatherPhenomenonExtraFee` (JSON, required): The weather phenomenon extra fee object containing
    String phenomenonCategoryCode - "RAIN", "SNOW OR SLEET", "THUNDER, GLAZE OR HAIL"
    private Set<VehicleType> applicableVehicles (vehicleType_id)
    Float extraFee
    Boolean forbidden
    LocalDateTime effectiveDate
    Boolean isActive

- **Responses:**
  - `201 CREATED`: Returns the saved weather phenomenon extra fee object.
  - `400 BAD REQUEST`: Returns an error message if the weather phenomenon extra fee could not be saved.

## Adding database entries

### Add City

**POST** `/city`

Add a new city to the database.

- **Request Body:**
  - `City` (JSON, required): The city object containing parameters String city, int wmoCode

- **Responses:**
  - `201 CREATED`: Returns the saved city object.
  - `400 BAD REQUEST`: Returns an error message if the city could not be saved.

### Add Vehicle Type

**POST** `/vehicleType`

Add a new vehicle type to the database.

- **Request Body:**
  - `VehicleType` (JSON, required): The vehicle type object containing details such as 
    String name
    boolean extraFeeApplicable - if vehicle belongs to a group of vehicles where extra fees based on weather conditions apply

- **Responses:**
  - `201 CREATED`: Returns the saved vehicle type object.
  - `400 BAD REQUEST`: Returns an error message if the vehicle type could not be saved.

### Add Weather Data

**POST** `/weatherData`

Add new weather data to the database.

- **Request Body:**
  - `WeatherData` (JSON, required): The weather data object containing details such as 
    String stationName
    int wmoCode
    Float airTemperature
    Float windSpeed
    String weatherPhenomenon
    int observationTimestamp

- **Responses:**
  - `201 CREATED`: Returns the saved weather data object.
  - `400 BAD REQUEST`: Returns an error message if the weather data could not be saved.

### Add Weather Phenomenon Type

**POST** `/weatherPhenomenonType`

Add a new weather phenomenon type to the database.

- **Request Body:**
  - `WeatherPhenomenonType` (JSON, required): The weather phenomenon type object containing details such as 
    String phenomenonName
    String weatherPhenomenonCategory

- **Responses:**
  - `201 CREATED`: Returns the saved weather phenomenon type object.
  - `400 BAD REQUEST`: Returns an error message if the weather phenomenon type could not be saved.

## Error Handling
All endpoints will return standard HTTP status codes to indicate success or failure. Detailed error messages will be provided in the response body for error statuses to aid in debugging.

## Example Requests

**Calculate Delivery Fee**
```bash
curl -X GET "http://localhost:8080/api/data/calculateDeliveryFee?city=Tartu&vehicleType=Bike&dateTime=2023-06-01T15:00:00"

curl -X GET "http://localhost:8080/api/data/calculateDeliveryFee?city=Tartu&vehicleType=Bike"

**Add City**
```bash
curl -X POST "http://localhost:8080/api/data/city" -H "Content-Type: application/json" -d '{"city": "Tartu", "wmoCode": 26242}'