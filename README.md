# Delivery Fee Calculator

## Description of the task

The objective is to develop a sub-functionality of the food delivery application, which
calculates the delivery fee for food couriers based on regional base fee, vehicle type, and weather
conditions.

### General requirements

	- End result will be evaluated regards to OOP design of the solution, layering, clean code, common patterns, etc
	- Code should be human readable
	- Public methods should be documented
	- Errors should be properly handled
	- Test should have relevant coverage

### Technologies to use (at minimum, but additional technologies can be used)

	- Java
	- Spring framework
	- H2 database


## Core modules

	1) Data model and database for storing data
	2) Configurable scheduled task for importing weather data (cronjob)
	3) Delivery fee calculation functions for calculating Base Fee, and if applicable, Air Temperature Extra Fee (ATEF), Wind Speed Extra Fee (WSEF), Weather Phenomenon Extra Fee (WPEF)
	4) REST interface for requesting delivery fee as per provided parameters
	5) Allow business rules for base fees and extra fees to be updated via REST interface (CRUD)

### Data model and database

There has to be at least 1 table in the database for weather data where the following business information on weather conditions is stored:
	- Name of the station
	- WMO code of the station
	- Air temperature
	- Wind speed
	- Weather phenomenon
	- Timestamp of the observations

Additional tables could be created in the database if it’s considered necessary to store classifications, parameters etc for fee calculation, etc.

### Cronjob

CronJob must be implemented to the code so that it requests weather data from the weather portal.
URL to request the data: https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php

The frequency of the cronjob has to be configurable. 
The default configuration to run the CronJob is once every hour, 15 minutes after a full hour(HH:15:00)

**Weather stations whose measurements should be stored to the database:**

	- Weather station: Tallinn-Harku, City: Tallinn, WMOCode: 26038
	- Weather station: Tartu-Tõravere, City: Tartu, WMOCode: 26242
	- Weather station: Pärnu, City: Pärnu, WMOCode: 41803

Imported data must be stored permanently in the database. Therefore, new entries must be inserted into the database as a result of each importing process. This means that for every cronjob run there should be 3 new entries created to the weather data table, one for each weather station.

#### Example data demonstrating the weather measurements data structure for a weather station

	<observations timestamp="1717675854">
		<station>
			<name>Virtsu</name> <!-- station name -->
			<wmocode>26128</wmocode> <!-- WMO code of the station -->
			<longitude>23.51355555534363</longitude> <!-- longitude coordinates of the station -->
			<latitude>58.572674999100215</latitude> <!-- latitude coordinates of the station -->
			<phenomenon>Light snowfall</phenomenon> <!-- weather phenomenon or cloudiness level -->
			<visibility>34.0</visibility> <!-- visibility (km) -->
			<precipitations>0</precipitations> <!-- precipitation (mm) in the last hour -->
			<airpressure>1005.4</airpressure> <!-- air pressure (hPa) -->
			<relativehumidity>57</relativehumidity> <!-- relative air humidity (%) -->
			<airtemperature>-3.6</airtemperature> <!-- air temperature (°C) -->
			<winddirection>101</winddirection> <!-- wind direction (°) -->
			<windspeed>3.2</windspeed> <!-- average wind speed (m/s) -->
			<windspeedmax>5.1</windspeedmax> <!-- maximum wind speed or gusts (m/s) -->
			<waterlevel>-49</waterlevel> <!-- inland water level (cm) relative to Amsterdam zero -->
			<waterlevel_eh2000>-28</waterlevel_eh2000> <!-- sea water level (cm) relative to Amsterdam zero -->
			<watertemperature>-0.2</watertemperature> <!-- water temperature (°C) -->
			<uvindex>3</uvindex> <!-- UV index -->
			<sunshineduration>63</sunshineduration> <!-- duration of sunshine (min) -->
			<globalradiation>207</globalradiation> <!-- total radiation, average per hour (W/m²) -->
		</station>
	</observations>


### Delivery Fee Calculations

A delivery fee has to be calculated according to input parameters from REST interface requests, weather data from the database, and business rules. 
The total delivery fee consists of a regional base fee for a specific vehicle types and extra fees for some weather conditions

#### Business rules to calculate regional base fee (RBF):
- **In case City = Tallinn:**
	- Vehicle type = Car, then RBF = 4 €
	- Vehicle type = Scooter, then RBF = 3.5 €
	- Vehicle type = Bike, then RBF = 3 €
- **In case City = Tartu:
	- Vehicle type = Car, then RBF = 3.5 €
	- Vehicle type = Scooter, then RBF = 3 €
	- Vehicle type = Bike, then RBF = 2.5 €
- **In case City = Pärnu:
	- Vehicle type = Car, then RBF = 3 €
	- Vehicle type = Scooter, then RBF = 2.5 €
	- Vehicle type = Bike, then RBF = 2 €

Extra fees for weather conditions are paid only in case any conditions listed below are valid.
Calculations must be based on the latest weather data for a specific city (most recent import).

#### Extra fee based on air temperature (ATEF) in a specific city is paid in case Vehicle type = "Scooter" or "Bike" if 
	- Air temperature in that City is less than -10̊ C, then ATEF = 1 €
	- Air temperature in that City is between -10̊ C and 0̊ C, then ATEF = 0.5 €
	
#### Extra fee based on wind speed (WSEF) in a specific city is paid in case Vehicle type = "Bike" if
	- Wind speed is between 10 m/s and 20 m/s, then WSEF = 0,5 €
	- In case of wind speed is greater than 20 m/s, then the error message “Usage of selected vehicle type is forbidden” has to be given
	
#### Extra fee based on weather phenomenon (WPEF) in a specific city is paid in case Vehicle type = "Scooter" or "Bike" if
	- Weather phenomenon is related to snow or sleet, then WPEF = 1 €
	- Weather phenomenon is related to rain, then WPEF = 0,5 €
	- In case the weather phenomenon is glaze, hail, or thunder, then the error message “Usage of selected vehicle type is forbidden” has to be given
	
#### Example calculations:
	1) Input parameters: TARTU and BIKE 
		- Latest weather data for Tartu (Tartu-Tõravere):
			- Air temperature = -2,1̊ C
			- Wind speed = 4,7 m/s 
			- Weather phenomenon = Light snow shower
		- RBF = 2,5 €
		- ATEF = 0,5 €
		- WSEF = 0 €
		- WPEF = 1 €
		- Total delivery fee (TDF) = RBF + ATEF + WSEF + WPEF = 2,5 + 0,5 + 0 + 1 = 4 €
	2) Input parameters: TALLINN and BIKE 
		- Latest weather data for Tallinn:
			- Air temperature = -2 C
			- Wind speed = 12 m/s 
			- Weather phenomenon = Glaze
		- RBF = 3 €
		- ATEF = 0.5 €
		- WSEF = 0.5 €
		- WPEF = Error: "Usage of selected vehicle type is forbidden"
		- Total delivery fee (TDF) = Error: "Usage of selected vehicle type is forbidden"

#### Wheather phenomena and their classifications

| Weather Phenomenon         | Phenomenon Category       |
|----------------------------|---------------------------|
| Clear                      | null                      |
| Few clouds                 | null                      |
| Variable clouds            | null                      |
| Cloudy with clear spells   | null                      |
| Overcast                   | null                      |
| Light snow shower          | SNOW OR SLEET             |
| Moderate snow shower       | SNOW OR SLEET             |
| Heavy snow shower          | SNOW OR SLEET             |
| Light shower               | RAIN                      |
| Moderate shower            | RAIN                      |
| Heavy shower               | RAIN                      |
| Light rain                 | RAIN                      |
| Moderate rain              | RAIN                      |
| Heavy rain                 | RAIN                      |
| Glaze                      | THUNDER, GLAZE OR HAIL    |
| Light sleet                | SNOW OR SLEET             |
| Moderate sleet             | SNOW OR SLEET             |
| Light snowfall             | SNOW OR SLEET             |
| Moderate snowfall          | SNOW OR SLEET             |
| Heavy snowfall             | SNOW OR SLEET             |
| Blowing snow               | SNOW OR SLEET             |
| Drifting snow              | SNOW OR SLEET             |
| Hail                       | THUNDER, GLAZE OR HAIL    |
| Mist                       | null                      |
| Fog                        | null                      |
| Thunder                    | THUNDER, GLAZE OR HAIL    |
| Thunderstorm               | THUNDER, GLAZE OR HAIL    |


### REST interface
REST interface (endpoint), which enables other parts of the application to request delivery fees according to the following input parameters:
	- City: Tallinn / Tartu / Pärnu
	- Vehicle type: Car / Scooter / Bike

In response to the request, the total delivery fee (calculated according to the Business Rules) or an error message must be given.

Interface must be Documented
