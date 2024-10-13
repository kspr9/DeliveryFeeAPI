# Delivery Fee API Documentation

## Table of Contents
1. [OpenAPI Documentation](#openapi-documentation)
2. [Running the App](#running-the-app)
3. [Base URL](#base-url)
4. [Data Models](#data-models)
5. [API Endpoints](#api-endpoints)  
   5.1 [Delivery Fee API](#delivery-fee-api)  
   5.2 [Data API](#data-api)  
   5.3 [Weather Data API](#weather-data-api)  
   5.4 [Base Fee API](#base-fee-api)  
   5.5 [Extra Fee API](#extra-fee-api)  
6. [Data Transfer Objects (DTOs)](#data-transfer-objects-dtos)  
7. [API call Examples](#api-call-examples)  

## OpenAPI documentation
This project has active OpenAPI documentation. It can be accessed once the application is running visiting `localhost:8080/swagger-ui/index.html` via the browser

## Running the App
This project has been made with Maven.  
To start the application first run `mvn clean install` from the root directory in the terminal.  
After that start the application by running `mvn spring-boot:run`

## Base URL
http://localhost:8080/api

## Data Models

### City
| Field   | Type    | Description                           |
|---------|---------|---------------------------------------|
| id      | Long    | The ID of the city                    |
| name    | String  | The name of the city                  |
| wmoCode | Integer | The WMO code associated with the city |

### VehicleType
| Field               | Type    | Description                                      |
|---------------------|---------|--------------------------------------------------|
| id                  | Long    | The ID of the vehicle type                       |
| name                | String  | The name of the vehicle type                     |
| extraFeeApplicable  | Boolean | Whether extra fees are applicable to this vehicle|

### WeatherData
| Field                 | Type    | Description                                      |
|-----------------------|---------|--------------------------------------------------|
| id                    | Long    | The ID of the weather data entry                 |
| stationName           | String  | The name of the weather station                  |
| wmoCode               | Integer | The WMO code of the weather station              |
| airTemperature        | Float   | The air temperature in degrees Celsius           |
| windSpeed             | Float   | The wind speed in meters per second              |
| weatherPhenomenon     | String  | The observed weather phenomenon                  |
| observationTimestamp  | Integer | The timestamp of the observation (Unix timestamp)|

### WeatherPhenomenonType
| Field      | Type                      | Description                           |
|------------|---------------------------|---------------------------------------|
| id         | Long                      | The ID of the weather phenomenon type |
| phenomenon | String                    | The name of the weather phenomenon    |
| category   | WeatherPhenomenonCategory | The category of the weather phenomenon|

### WeatherPhenomenonCategory
Enum with the following values:
- NONE
- RAIN
- SNOW_OR_SLEET
- THUNDER_GLAZE_OR_HAIL

## API Endpoints

### Delivery Fee Controller
Base path: `/delivery`

#### Calculate Delivery Fee
Calculates the delivery fee based on city, vehicle type, and optionally a specific date and time.
- **GET** `/city/{city}/vehicle/{vehicleType}`
- **Parameters:**
  - `city` (String, required): The name of the city.
  - `vehicleType` (String, required): The type of vehicle.
  - `dateTime` (LocalDateTime, *optional*): The date and time for the request. Used to match ruling weather conditions at the time of delivery.
- **Responses:** 
     - `200 OK`: Calculated delivery fee as a number with two decimal places (200 OK). If dateTime not provided, fee is calculated based on the latest weather data (LocalDateTime.now()). If dateTime is provided, fee is calculated based on the weather conditions at that time.
     - `403 FORBIDDEN`: Returns an error message "Usage of selected vehicle type is forbidden" if usage of selected vehicle is forbidden for given weather conditions.

**Example Request:**
```bash
curl -X GET "http://localhost:8080/api/delivery/city/Tallinn/vehicle/Car?dateTime=2023-06-10T14:30:00"
curl -X GET "http://localhost:8080/api/delivery/city/Tallinn/vehicle/Car"
```

### Weather Data Controller
Base path: `/weather`

- **POST** `/import`: Manually trigger weather data import
- **GET** `/`: Retrieve all weather data, returns a list of WeatherDataDTO
- **POST** `/`: Create new weather data
- **GET** `/{id}`: Retrieve specific weather data, returns a WeatherDataDTO

### Data Controller
Base path: `/data`

#### Cities
- **GET** `/cities`: Retrieve all cities
- **POST** `/cities`: Add a new city
- **GET** `/cities/{id}`: Retrieve a specific city
- **PUT** `/cities/{id}`: Update a specific city
- **DELETE** `/cities/{id}`: Delete a specific city

#### Vehicle Types
- **GET** `/vehicleTypes`: Retrieve all vehicle types
- **POST** `/vehicleTypes`: Add a new vehicle type
- **GET** `/vehicleTypes/{id}`: Retrieve a specific vehicle type
- **PUT** `/vehicleTypes/{id}`: Update a specific vehicle type
- **DELETE** `/vehicleTypes/{id}`: Delete a specific vehicle type

#### Weather Phenomenon Types
- **GET** `/weatherPhenomenonTypes`: Retrieve all weather phenomenon types
- **POST** `/weatherPhenomenonTypes`: Add a new weather phenomenon type
- **GET** `/weatherPhenomenonTypes/{id}`: Retrieve a specific weather phenomenon type
- **PUT** `/weatherPhenomenonTypes/{id}`: Update a specific weather phenomenon type
- **DELETE** `/weatherPhenomenonTypes/{id}`: Delete a specific weather phenomenon type

#### Weather Data
- **GET** `/weatherData`: Retrieve all weather data
- **Response:** A list of WeatherData objects, including observationTimestampAsLocalDateTime


### Base Fee Controller
Base path: `/fees/base_fee`

- **GET** `/`: Retrieve all base fees
- **POST** `/`: Create a new base fee
- **PUT** `/{id}`: Update an existing base fee
- **DELETE** `/{id}`: Delete a base fee

### Extra Fee Controller
Base path: `/fees/extra_fee`

#### Air Temperature Extra Fee
- **GET** `/air_temperature`: Retrieve all air temperature extra fees
- **POST** `/air_temperature`: Create a new air temperature extra fee
- **PUT** `/air_temperature/{id}`: Update an existing air temperature extra fee
- **DELETE** `/air_temperature/{id}`: Delete an air temperature extra fee

#### Wind Speed Extra Fee
- **GET** `/wind_speed`: Retrieve all wind speed extra fees
- **POST** `/wind_speed`: Create a new wind speed extra fee
- **PUT** `/wind_speed/{id}`: Update an existing wind speed extra fee
- **DELETE** `/wind_speed/{id}`: Delete a wind speed extra fee

#### Weather Phenomenon Extra Fee
- **GET** `/weather_phenomenon`: Retrieve all weather phenomenon extra fees
- **POST** `/weather_phenomenon`: Create a new weather phenomenon extra fee
- **PUT** `/weather_phenomenon/{id}`: Update an existing weather phenomenon extra fee
- **DELETE** `/weather_phenomenon/{id}`: Delete a weather phenomenon extra fee


## Data Transfer Objects (DTOs)

### BaseFeeDTO
| Field          | Type          | Description                                      |
|----------------|---------------|--------------------------------------------------|
| cityId         | Long          | The ID of the city                               |
| vehicleTypeId  | Long          | The ID of the vehicle type                       |
| baseFee        | BigDecimal    | The base fee amount                              |
| effectiveDate  | LocalDateTime | The date and time from when the fee is effective |
| isActive       | Boolean       | Whether the fee is currently active              |

### AirTemperatureExtraFeeDTO
| Field                | Type          | Description                                     |
|----------------------|---------------|-------------------------------------------------|
| id                   | Long          | The ID of the air temperature extra fee         |
| minTemp              | Float         | The lower bound of temperature range for extra fee |
| maxTemp              | Float         | The upper bound of temperature range for extra fee |
| applicableVehicleIds | Set<Long>     | The IDs of vehicle types this fee applies to    |
| extraFee             | BigDecimal    | The extra fee amount                            |
| effectiveDate        | LocalDateTime | The date and time when the fee becomes effective|
| isActive             | Boolean       | Whether the fee is currently active             |

### WindSpeedExtraFeeDTO
| Field                | Type          | Description                                       |
|----------------------|---------------|---------------------------------------------------|
| id                   | Long          | The ID of the wind speed extra fee                |
| minSpeed             | Float         | The minimum wind speed for this fee               |
| maxSpeed             | Float         | The maximum wind speed for this fee               |
| applicableVehicleIds | Set<Long>     | The IDs of vehicle types this fee applies to      |
| extraFee             | BigDecimal    | The extra fee amount                              |
| forbidden            | Boolean       | Whether usage is forbidden under these conditions |
| effectiveDate        | LocalDateTime | The date and time when the fee becomes effective  |
| isActive             | Boolean       | Whether the fee is currently active               |

### WeatherPhenomenonExtraFeeDTO
| Field                | Type                      | Description                                       |
|----------------------|---------------------------|---------------------------------------------------|
| id                   | Long                      | The ID of the weather phenomenon extra fee        |
| phenomenonCategory   | WeatherPhenomenonCategory | The category of weather phenomenon                |
| applicableVehicleIds | Set<Long>                 | The IDs of vehicle types this fee applies to      |
| extraFee             | BigDecimal                | The extra fee amount                              |
| forbidden            | Boolean                   | Whether usage is forbidden under these conditions |
| effectiveDate        | LocalDateTime             | The date and time when the fee becomes effective  |
| isActive             | Boolean                   | Whether the fee is currently active               |

### WeatherDataDTO
| Field                 | Type    | Description                                      |
|-----------------------|---------|--------------------------------------------------|
| id                    | Long    | The ID of the weather data entry                 |
| stationName           | String  | The name of the weather station                  |
| wmoCode               | Integer | The WMO code of the weather station              |
| airTemperature        | Float   | The air temperature in degrees Celsius           |
| windSpeed             | Float   | The wind speed in meters per second              |
| weatherPhenomenon     | String  | The observed weather phenomenon                  |
| observationTimestamp  | Integer | The timestamp of the observation (Unix timestamp)|


## API call Examples

This section provides example requests for each endpoint in the Delivery Fee API.

## Delivery Fee API

#### Calculate Delivery Fee

```bash
curl -X GET "http://localhost:8080/api/delivery/city/Tallinn/vehicle/Car?dateTime=2023-06-10T14:30:00"
```

## Weather Data API

#### Import Weather Data
```bash
curl -X POST "http://localhost:8080/api/weather/import"
```

#### Get All Weather Data
```bash
curl -X GET "http://localhost:8080/api/weather"
```

#### Create New Weather Data
```bash
curl -X POST "http://localhost:8080/api/weather" \
     -H "Content-Type: application/json" \
     -d '{
       "stationName": "Tallinn-Harku",
       "wmoCode": 26038,
       "airTemperature": 22.5,
       "windSpeed": 3.2,
       "weatherPhenomenon": "Clear sky",
       "observationTimestamp": 1623456789
     }'
```

#### Get Weather Data by ID
```bash
curl -X GET "http://localhost:8080/api/weather/1"
```

## Data Controller

### Cities

#### Get All Cities
```bash
curl -X GET "http://localhost:8080/api/data/cities"
```

#### Add New City
```bash
curl -X POST "http://localhost:8080/api/data/cities" \
     -H "Content-Type: application/json" \
     -d '{"name": "Helsinki", "wmoCode": 2978}'
```

#### Get City by ID
```bash
curl -X GET "http://localhost:8080/api/data/cities/1"
```

#### Update City
```bash
curl -X PUT "http://localhost:8080/api/data/cities/1" \
     -H "Content-Type: application/json" \
     -d '{"name": "Helsinki", "wmoCode": 2979}'
```

#### Delete City
```bash
curl -X DELETE "http://localhost:8080/api/data/cities/1"
```

### Vehicle Types

#### Get All Vehicle Types
```bash
curl -X GET "http://localhost:8080/api/data/vehicleTypes"
```

#### Add New Vehicle Type
```bash
curl -X POST "http://localhost:8080/api/data/vehicleTypes" \
     -H "Content-Type: application/json" \
     -d '{"name": "Electric Scooter", "extraFeeApplicable": true}'
```

#### Get Vehicle Type by ID
```bash
curl -X GET "http://localhost:8080/api/data/vehicleTypes/1"
```

#### Update Vehicle Type
```bash
curl -X PUT "http://localhost:8080/api/data/vehicleTypes/1" \
     -H "Content-Type: application/json" \
     -d '{"name": "Electric Scooter", "extraFeeApplicable": false}'
```

#### Delete Vehicle Type
```bash
curl -X DELETE "http://localhost:8080/api/data/vehicleTypes/1"
```

### Weather Phenomenon Types

#### Get All Weather Phenomenon Types
```bash
curl -X GET "http://localhost:8080/api/data/weatherPhenomenonTypes"
```

#### Add New Weather Phenomenon Type
```bash
curl -X POST "http://localhost:8080/api/data/weatherPhenomenonTypes" \
     -H "Content-Type: application/json" \
     -d '{"phenomenon": "Heavy Fog", "category": "NONE"}'
```

#### Get Weather Phenomenon Type by ID
```bash
curl -X GET "http://localhost:8080/api/data/weatherPhenomenonTypes/1"
```

#### Update Weather Phenomenon Type
```bash
curl -X PUT "http://localhost:8080/api/data/weatherPhenomenonTypes/1" \
     -H "Content-Type: application/json" \
     -d '{"phenomenon": "Dense Fog", "category": "NONE"}'
```

#### Delete Weather Phenomenon Type
```bash
curl -X DELETE "http://localhost:8080/api/data/weatherPhenomenonTypes/1"
```

### Weather Data

#### Get All Weather Data
```bash
curl -X GET "http://localhost:8080/api/data/weatherData"
```

## Base Fee API

#### Get All Base Fees
```bash
curl -X GET "http://localhost:8080/api/fees/base_fee"
```

#### Create New Base Fee
```bash
curl -X POST "http://localhost:8080/api/fees/base_fee" \
     -H "Content-Type: application/json" \
     -d '{
       "cityId": 1,
       "vehicleTypeId": 2,
       "baseFee": 3.5,
       "effectiveDate": "2023-06-01T00:00:00",
       "isActive": true
     }'
```

#### Update Base Fee
```bash
curl -X PUT "http://localhost:8080/api/fees/base_fee/1" \
     -H "Content-Type: application/json" \
     -d '{
       "cityId": 1,
       "vehicleTypeId": 2,
       "baseFee": 4.0,
       "effectiveDate": "2023-07-01T00:00:00",
       "isActive": true
     }'
```

#### Delete Base Fee
```bash
curl -X DELETE "http://localhost:8080/api/fees/base_fee/1"
```

## Extra Fee API

### Air Temperature Extra Fee

#### Get All Air Temperature Extra Fees
```bash
curl -X GET "http://localhost:8080/api/fees/extra_fee/air_temperature"
```

#### Create New Air Temperature Extra Fee
```bash
curl -X POST "http://localhost:8080/api/fees/extra_fee/air_temperature" \
     -H "Content-Type: application/json" \
     -d '{
       "minTemp": -10.0,
       "maxTemp": 0.0,
       "applicableVehicleIds": [2, 3],
       "extraFee": 1.0,
       "effectiveDate": "2023-06-01T00:00:00",
       "isActive": true
     }'
```

#### Update Air Temperature Extra Fee
```bash
curl -X PUT "http://localhost:8080/api/fees/extra_fee/air_temperature/1" \
     -H "Content-Type: application/json" \
     -d '{
       "minTemp": -15.0,
       "maxTemp": -5.0,
       "applicableVehicleIds": [2, 3],
       "extraFee": 1.5,
       "effectiveDate": "2023-07-01T00:00:00",
       "isActive": true
     }'
```

#### Delete Air Temperature Extra Fee
```bash
curl -X DELETE "http://localhost:8080/api/fees/extra_fee/air_temperature/1"
```

### Wind Speed Extra Fee

#### Get All Wind Speed Extra Fees
```bash
curl -X GET "http://localhost:8080/api/fees/extra_fee/wind_speed"
```

#### Create New Wind Speed Extra Fee
```bash
curl -X POST "http://localhost:8080/api/fees/extra_fee/wind_speed" \
     -H "Content-Type: application/json" \
     -d '{
       "minSpeed": 10.0,
       "maxSpeed": 20.0,
       "applicableVehicleIds": [2, 3],
       "extraFee": 0.5,
       "forbidden": false,
       "effectiveDate": "2023-06-01T00:00:00",
       "isActive": true
     }'
```

#### Update Wind Speed Extra Fee
```bash
curl -X PUT "http://localhost:8080/api/fees/extra_fee/wind_speed/1" \
     -H "Content-Type: application/json" \
     -d '{
       "minSpeed": 15.0,
       "maxSpeed": 25.0,
       "applicableVehicleIds": [2, 3],
       "extraFee": 1.0,
       "forbidden": false,
       "effectiveDate": "2023-07-01T00:00:00",
       "isActive": true
     }'
```

#### Delete Wind Speed Extra Fee
```bash
curl -X DELETE "http://localhost:8080/api/fees/extra_fee/wind_speed/1"
```

### Weather Phenomenon Extra Fee

#### Get All Weather Phenomenon Extra Fees
```bash
curl -X GET "http://localhost:8080/api/fees/extra_fee/weather_phenomenon"
```

#### Create New Weather Phenomenon Extra Fee
```bash
curl -X POST "http://localhost:8080/api/fees/extra_fee/weather_phenomenon" \
     -H "Content-Type: application/json" \
     -d '{
       "phenomenonCategory": "SNOW_OR_SLEET",
       "applicableVehicleIds": [2, 3],
       "extraFee": 1.0,
       "forbidden": false,
       "effectiveDate": "2023-06-01T00:00:00",
       "isActive": true
     }'
```

#### Update Weather Phenomenon Extra Fee
```bash
curl -X PUT "http://localhost:8080/api/fees/extra_fee/weather_phenomenon/1" \
     -H "Content-Type: application/json" \
     -d '{
       "phenomenonCategory": "THUNDER_GLAZE_OR_HAIL",
       "applicableVehicleIds": [2, 3],
       "extraFee": null,
       "forbidden": true,
       "effectiveDate": "2023-07-01T00:00:00",
       "isActive": true
     }'
```

#### Delete Weather Phenomenon Extra Fee
```bash
curl -X DELETE "http://localhost:8080/api/fees/extra_fee/weather_phenomenon/1"
```

