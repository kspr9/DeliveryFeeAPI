package com.fujitsu.delivery_fee_api.service;


import com.fujitsu.delivery_fee_api.exception.VehicleUsageForbiddenException;
import com.fujitsu.delivery_fee_api.model.*;

import com.fujitsu.delivery_fee_api.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest
@ActiveProfiles("test")
class DeliveryFeeCalculationServiceTest {


    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;
    @Autowired
    private WeatherPhenomenonTypeRepository weatherPhenomenonTypeRepository;

    @Autowired
    private DeliveryFeeCalculationService deliveryFeeCalculationService;
    
    @MockBean
    private WeatherDataRepository weatherDataRepository;

    @BeforeEach
    void setUp() {
        
    }

    @Test
    void testInitializedData() {
        List<City> cities = cityRepository.findAll();
        assertFalse(cities.isEmpty(), "Cities should be initialized");

        List<VehicleType> vehicleTypes = vehicleTypeRepository.findAll();
        assertFalse(vehicleTypes.isEmpty(), "Vehicle types should be initialized");

        List<WeatherPhenomenonType> phenomenonTypes = weatherPhenomenonTypeRepository.findAll();
        assertFalse(phenomenonTypes.isEmpty(), "Weather phenomenon types should be initialized");
    }

    @Test
    void calculateDeliveryFee_CarInTallinnWithNormalWeather_ReturnsExpectedFee() {
        // Arrange
        String cityName = "Tallinn";
        String vehicleType = "Car";
        LocalDateTime timestamp = LocalDateTime.of(2023, 6, 15, 12, 0);
        WeatherData weatherData = new WeatherData("Tallinn-Harku", 26038, 15.0f, 3.0f, "Clear", 1683859200); // May 10, 2023
        when(weatherDataRepository.findLatestByWMOCodeAsOfOpt(eq(26038), anyInt())).thenReturn(Optional.of(weatherData));

        // Act
        BigDecimal deliveryFee = deliveryFeeCalculationService.calculateDeliveryFee(cityName, vehicleType, timestamp);

        // Assert
        assertEquals(new BigDecimal("4.00"), deliveryFee);
    }

    @Test
    void calculateDeliveryFee_ScooterInTartu_ReturnsExpectedFee() {
        // Arrange
        String cityName = "Tartu";
        String vehicleType = "Scooter";
        LocalDateTime timestamp = LocalDateTime.of(2023, 6, 15, 12, 0);
        WeatherData weatherData = new WeatherData("Tartu-Tõravere", 26242, 15.0f, 3.0f, "Clear", 1686823200);
        when(weatherDataRepository.findLatestByWMOCodeAsOfOpt(eq(26242), anyInt())).thenReturn(Optional.of(weatherData));

        // Act
        BigDecimal deliveryFee = deliveryFeeCalculationService.calculateDeliveryFee(cityName, vehicleType, timestamp);

        // Assert
        assertEquals(new BigDecimal("3.00"), deliveryFee);
    }

    @Test
    void calculateDeliveryFee_BikeInParnu_ReturnsExpectedFee() {
        // Arrange
        String cityName = "Pärnu";
        String vehicleType = "Bike";
        LocalDateTime timestamp = LocalDateTime.of(2023, 6, 15, 12, 0);
        WeatherData weatherData = new WeatherData("Pärnu", 41803, 15.0f, 3.0f, "Clear", 1686823200);
        when(weatherDataRepository.findLatestByWMOCodeAsOfOpt(eq(41803), anyInt())).thenReturn(Optional.of(weatherData));

        // Act
        BigDecimal deliveryFee = deliveryFeeCalculationService.calculateDeliveryFee(cityName, vehicleType, timestamp);

        // Assert
        assertEquals(new BigDecimal("2.00"), deliveryFee);
    }

    @Test
    void calculateDeliveryFee_CarWithExtremeWeather_NoExtraFees() {
        // Arrange
        String cityName = "Tallinn";
        String vehicleType = "Car";
        LocalDateTime timestamp = LocalDateTime.of(2023, 1, 15, 12, 0);
        WeatherData weatherData = new WeatherData("Tallinn-Harku", 26038, -15.0f, 22.0f, "Heavy snowfall", 1673784000);
        when(weatherDataRepository.findLatestByWMOCodeAsOfOpt(eq(26038), anyInt())).thenReturn(Optional.of(weatherData));

        // Act
        BigDecimal deliveryFee = deliveryFeeCalculationService.calculateDeliveryFee(cityName, vehicleType, timestamp);

        // Assert
        assertEquals(new BigDecimal("4.00"), deliveryFee);
    }

    @Test
    void calculateDeliveryFee_ScooterWithTemperatureBelowMinus10_AppliesExtraFee() {
        // Arrange
        String cityName = "Tallinn";
        String vehicleType = "Scooter";
        LocalDateTime timestamp = LocalDateTime.of(2023, 1, 15, 12, 0);
        WeatherData weatherData = new WeatherData("Tallinn-Harku", 26038, -12.0f, 5.0f, "Clear", 1673784000);
        when(weatherDataRepository.findLatestByWMOCodeAsOfOpt(eq(26038), anyInt())).thenReturn(Optional.of(weatherData));

        // Act
        BigDecimal deliveryFee = deliveryFeeCalculationService.calculateDeliveryFee(cityName, vehicleType, timestamp);

        // Assert
        assertEquals(new BigDecimal("4.50"), deliveryFee); // Base fee 3.5 + ATEF 1.0
    }

    @Test
    void calculateDeliveryFee_ScooterWithTemperatureBetweenMinus10And0_AppliesExtraFee() {
        // Arrange
        String cityName = "Tallinn";
        String vehicleType = "Scooter";
        LocalDateTime timestamp = LocalDateTime.of(2023, 1, 15, 12, 0);
        WeatherData weatherData = new WeatherData("Tallinn-Harku", 26038, -5.0f, 5.0f, "Clear", 1673784000);
        when(weatherDataRepository.findLatestByWMOCodeAsOfOpt(eq(26038), anyInt())).thenReturn(Optional.of(weatherData));

        // Act
        BigDecimal deliveryFee = deliveryFeeCalculationService.calculateDeliveryFee(cityName, vehicleType, timestamp);

        // Assert
        assertEquals(new BigDecimal("4.00"), deliveryFee); // Base fee 3.5 + ATEF 0.5
    }

    @Test
    void calculateDeliveryFee_ScooterWithTemperatureAbove0_NoExtraFee() {
        // Arrange
        String cityName = "Tallinn";
        String vehicleType = "Scooter";
        LocalDateTime timestamp = LocalDateTime.of(2023, 6, 15, 12, 0);
        WeatherData weatherData = new WeatherData("Tallinn-Harku", 26038, 15.0f, 5.0f, "Clear", 1686823200);
        when(weatherDataRepository.findLatestByWMOCodeAsOfOpt(eq(26038), anyInt())).thenReturn(Optional.of(weatherData));

        // Act
        BigDecimal deliveryFee = deliveryFeeCalculationService.calculateDeliveryFee(cityName, vehicleType, timestamp);

        // Assert
        assertEquals(new BigDecimal("3.50"), deliveryFee); // Base fee 3.5, no ATEF
    }

    @Test
    void calculateDeliveryFee_BikeWithWindSpeedBetween10And20_AppliesExtraFee() {
        // Arrange
        String cityName = "Tallinn";
        String vehicleType = "Bike";
        LocalDateTime timestamp = LocalDateTime.of(2023, 6, 15, 12, 0);
        WeatherData weatherData = new WeatherData("Tallinn-Harku", 26038, 15.0f, 15.0f, "Clear", 1686823200);
        when(weatherDataRepository.findLatestByWMOCodeAsOfOpt(eq(26038), anyInt())).thenReturn(Optional.of(weatherData));

        // Act
        BigDecimal deliveryFee = deliveryFeeCalculationService.calculateDeliveryFee(cityName, vehicleType, timestamp);

        // Assert
        assertEquals(new BigDecimal("3.50"), deliveryFee); // Base fee 3.0 + WSEF 0.5
    }

    @Test
    void calculateDeliveryFee_BikeWithWindSpeedAbove20_ThrowsException() {
        // Arrange
        String cityName = "Tallinn";
        String vehicleType = "Bike";
        LocalDateTime timestamp = LocalDateTime.of(2023, 6, 15, 12, 0);
        WeatherData weatherData = new WeatherData("Tallinn-Harku", 26038, 15.0f, 22.0f, "Clear", 1686823200);
        when(weatherDataRepository.findLatestByWMOCodeAsOfOpt(eq(26038), anyInt())).thenReturn(Optional.of(weatherData));

        // Act & Assert
        assertThrows(VehicleUsageForbiddenException.class, () -> 
            deliveryFeeCalculationService.calculateDeliveryFee(cityName, vehicleType, timestamp)
        );
    }

    @Test
    void calculateDeliveryFee_BikeWithWindSpeedBelow10_NoExtraFee() {
        // Arrange
        String cityName = "Tallinn";
        String vehicleType = "Bike";
        LocalDateTime timestamp = LocalDateTime.of(2023, 6, 15, 12, 0);
        WeatherData weatherData = new WeatherData("Tallinn-Harku", 26038, 15.0f, 5.0f, "Clear", 1686823200);
        when(weatherDataRepository.findLatestByWMOCodeAsOfOpt(eq(26038), anyInt())).thenReturn(Optional.of(weatherData));

        // Act
        BigDecimal deliveryFee = deliveryFeeCalculationService.calculateDeliveryFee(cityName, vehicleType, timestamp);

        // Assert
        assertEquals(new BigDecimal("3.00"), deliveryFee); // Base fee 3.0, no WSEF
    }

    @Test
    void calculateDeliveryFee_ScooterWithSnowOrSleet_AppliesExtraFee() {
        // Arrange
        String cityName = "Tallinn";
        String vehicleType = "Scooter";
        LocalDateTime timestamp = LocalDateTime.of(2023, 1, 15, 12, 0);
        WeatherData weatherData = new WeatherData("Tallinn-Harku", 26038, 0.1f, 5.0f, "Light snow shower", 1673784000);
        when(weatherDataRepository.findLatestByWMOCodeAsOfOpt(eq(26038), anyInt())).thenReturn(Optional.of(weatherData));

        // Act
        BigDecimal deliveryFee = deliveryFeeCalculationService.calculateDeliveryFee(cityName, vehicleType, timestamp);

        // Assert
        assertEquals(new BigDecimal("4.50"), deliveryFee); // Base fee 3.5 + WPEF 1.0
    }

    @Test
    void calculateDeliveryFee_BikeWithRain_AppliesExtraFee() {
        // Arrange
        String cityName = "Tallinn";
        String vehicleType = "Bike";
        LocalDateTime timestamp = LocalDateTime.of(2023, 6, 15, 12, 0);
        WeatherData weatherData = new WeatherData("Tallinn-Harku", 26038, 15.0f, 5.0f, "Light rain", 1686823200);
        when(weatherDataRepository.findLatestByWMOCodeAsOfOpt(eq(26038), anyInt())).thenReturn(Optional.of(weatherData));

        // Act
        BigDecimal deliveryFee = deliveryFeeCalculationService.calculateDeliveryFee(cityName, vehicleType, timestamp);

        // Assert
        assertEquals(new BigDecimal("3.50"), deliveryFee); // Base fee 3.0 + WPEF 0.5
    }

    @Test
    void calculateDeliveryFee_ScooterWithThunder_ThrowsException() {
        // Arrange
        String cityName = "Tallinn";
        String vehicleType = "Scooter";
        LocalDateTime timestamp = LocalDateTime.of(2023, 6, 15, 12, 0);
        WeatherData weatherData = new WeatherData("Tallinn-Harku", 26038, 20.0f, 5.0f, "Thunder", 1686823200);
        when(weatherDataRepository.findLatestByWMOCodeAsOfOpt(eq(26038), anyInt())).thenReturn(Optional.of(weatherData));

        // Act & Assert
        assertThrows(VehicleUsageForbiddenException.class, () -> 
            deliveryFeeCalculationService.calculateDeliveryFee(cityName, vehicleType, timestamp)
        );
    }

    @Test
    void calculateDeliveryFee_BikeWithGlaze_ThrowsException() {
        // Arrange
        String cityName = "Tallinn";
        String vehicleType = "Bike";
        LocalDateTime timestamp = LocalDateTime.of(2023, 1, 15, 12, 0);
        WeatherData weatherData = new WeatherData("Tallinn-Harku", 26038, -2.0f, 5.0f, "Glaze", 1673784000);
        when(weatherDataRepository.findLatestByWMOCodeAsOfOpt(eq(26038), anyInt())).thenReturn(Optional.of(weatherData));

        // Act & Assert
        assertThrows(VehicleUsageForbiddenException.class, () -> 
            deliveryFeeCalculationService.calculateDeliveryFee(cityName, vehicleType, timestamp)
        );
    }

    @Test
    void calculateDeliveryFee_ScooterWithHail_ThrowsException() {
        // Arrange
        String cityName = "Tallinn";
        String vehicleType = "Scooter";
        LocalDateTime timestamp = LocalDateTime.of(2023, 6, 15, 12, 0);
        WeatherData weatherData = new WeatherData("Tallinn-Harku", 26038, 15.0f, 5.0f, "Hail", 1686823200);
        when(weatherDataRepository.findLatestByWMOCodeAsOfOpt(eq(26038), anyInt())).thenReturn(Optional.of(weatherData));

        // Act & Assert
        assertThrows(VehicleUsageForbiddenException.class, () -> 
            deliveryFeeCalculationService.calculateDeliveryFee(cityName, vehicleType, timestamp)
        );
    }

    @Test
    void calculateDeliveryFee_BikeWithWindSpeedAndLowTemperature_AppliesMultipleExtraFees() {
        // Arrange
        String cityName = "Tallinn";
        String vehicleType = "Bike";
        LocalDateTime timestamp = LocalDateTime.of(2023, 1, 15, 12, 0);
        WeatherData weatherData = new WeatherData("Tallinn-Harku", 26038, -5.0f, 15.0f, "Clear", 1673784000);
        when(weatherDataRepository.findLatestByWMOCodeAsOfOpt(eq(26038), anyInt())).thenReturn(Optional.of(weatherData));

        // Act
        BigDecimal deliveryFee = deliveryFeeCalculationService.calculateDeliveryFee(cityName, vehicleType, timestamp);

        // Assert
        assertEquals(new BigDecimal("4.00"), deliveryFee); // Base fee 3.0 + WSEF 0.5 + ATEF 0.5
    }

    @Test
    void calculateDeliveryFee_ScooterWithLowTemperatureAndRain_AppliesMultipleExtraFees() {
        // Arrange
        String cityName = "Tallinn";
        String vehicleType = "Scooter";
        LocalDateTime timestamp = LocalDateTime.of(2023, 1, 15, 12, 0);
        WeatherData weatherData = new WeatherData("Tallinn-Harku", 26038, -5.0f, 5.0f, "Light rain", 1673784000);
        when(weatherDataRepository.findLatestByWMOCodeAsOfOpt(eq(26038), anyInt())).thenReturn(Optional.of(weatherData));

        // Act
        BigDecimal deliveryFee = deliveryFeeCalculationService.calculateDeliveryFee(cityName, vehicleType, timestamp);

        // Assert
        assertEquals(new BigDecimal("4.50"), deliveryFee); // Base fee 3.5 + ATEF 0.5 + WPEF 0.5
    }

    @Test
    void calculateDeliveryFee_BikeWithHighWindSpeedAndLowTemperature_ThrowsException() {
        // Arrange
        String cityName = "Tallinn";
        String vehicleType = "Bike";
        LocalDateTime timestamp = LocalDateTime.of(2023, 1, 15, 12, 0);
        WeatherData weatherData = new WeatherData("Tallinn-Harku", 26038, -5.0f, 22.0f, "Clear", 1673784000);
        when(weatherDataRepository.findLatestByWMOCodeAsOfOpt(eq(26038), anyInt())).thenReturn(Optional.of(weatherData));

        // Act & Assert
        assertThrows(VehicleUsageForbiddenException.class, () -> 
            deliveryFeeCalculationService.calculateDeliveryFee(cityName, vehicleType, timestamp)
        );
    }

    @Test
    void calculateDeliveryFee_BikeWithHighWindSpeedAndThunder_ThrowsException() {
        // Arrange
        String cityName = "Tallinn";
        String vehicleType = "Bike";
        LocalDateTime timestamp = LocalDateTime.of(2023, 6, 15, 12, 0);
        WeatherData weatherData = new WeatherData("Tallinn-Harku", 26038, 20.0f, 22.0f, "Thunder", 1686823200);
        when(weatherDataRepository.findLatestByWMOCodeAsOfOpt(eq(26038), anyInt())).thenReturn(Optional.of(weatherData));

        // Act & Assert
        assertThrows(VehicleUsageForbiddenException.class, () -> 
            deliveryFeeCalculationService.calculateDeliveryFee(cityName, vehicleType, timestamp)
        );
    }

    @Test
    void calculateDeliveryFee_ScooterWithLowTemperatureWindAndSnow_AppliesMultipleExtraFees() {
        // Arrange
        String cityName = "Tallinn";
        String vehicleType = "Scooter";
        LocalDateTime timestamp = LocalDateTime.of(2023, 1, 15, 12, 0);
        WeatherData weatherData = new WeatherData("Tallinn-Harku", 26038, -5.0f, 15.0f, "Light snow shower", 1673784000);
        when(weatherDataRepository.findLatestByWMOCodeAsOfOpt(eq(26038), anyInt())).thenReturn(Optional.of(weatherData));

        // Act
        BigDecimal deliveryFee = deliveryFeeCalculationService.calculateDeliveryFee(cityName, vehicleType, timestamp);

        // Assert
        assertEquals(new BigDecimal("5.00"), deliveryFee); // Base fee 3.5 + ATEF 0.5 + WPEF 1.0
    }


}