package weatherapp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import weatherapp.controller.WeatherController;
import weatherapp.model.WeatherData;
import weatherapp.service.WeatherService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherControllerTest {

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private WeatherController weatherController;

    @Test
    void testReceiveWeatherData_Success() {
        Map<String, Object> weatherData = new HashMap<>();
        ResponseEntity<String> response = weatherController.receiveWeatherData(weatherData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Data received successfully", response.getBody());
    }

    @Test
    void testGetWeatherInfo_Success() {
        WeatherData mockWeatherData = new WeatherData();
        mockWeatherData.setCity("Rio de Janeiro");
        mockWeatherData.setDescription("Sunny");
        mockWeatherData.setTemperature(25.0);

        when(weatherService.getWeatherData(anyString(), anyString())).thenReturn(mockWeatherData);

        ResponseEntity<String> response = weatherController.getWeatherInfo("City", "Date");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Weather info retrieved successfully", response.getBody());
    }

    @Test
    void testGetWeatherInfo_Exception() {
        doThrow(RuntimeException.class).when(weatherService).getWeatherData(anyString(), anyString());

        ResponseEntity<String> response = weatherController.getWeatherInfo("Rio de janeiro", "2023-12-04");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error retrieving weather info", response.getBody());
    }

    @Test
    void testRemoveWeatherData_Success() {
        ResponseEntity<String> response = weatherController.removeWeatherData(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Data removed successfully", response.getBody());
    }


    @Test
    void testUpdateWeatherData_Success() {
        Map<String, Object> weatherData = new HashMap<>();
        ResponseEntity<String> response = weatherController.updateWeatherData(1L, weatherData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Data updated successfully", response.getBody());
    }

    @Test
    void testConsumeExternalApi_Success() {
        ResponseEntity<String> response = weatherController.consumeExternalApi();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("External API consumed successfully", response.getBody());
    }
}
