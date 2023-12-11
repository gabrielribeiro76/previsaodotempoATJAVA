package weatherapp.controller;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import weatherapp.model.WeatherData;
import weatherapp.service.WeatherService;
import weatherapp.util.ApiRequestUtil;

import java.util.Map;

@RestController
@RequestMapping("/api/weather")
@Slf4j
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @PostMapping("/data")
    public ResponseEntity<String> receiveWeatherData(@RequestBody Map<String, Object> weatherData) {
        try {

            log.info("Received weather data: {}", weatherData);

            return ResponseEntity.ok("Data received successfully");
        } catch (Exception e) {
            log.error("Error while processing weather data", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing weather data");
        }
    }

    @GetMapping("/info")
    public ResponseEntity<String> getWeatherInfo(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String date) {
        try {
            WeatherData weatherData = weatherService.getWeatherData(city, date);

            log.info("Getting weather info for city: {}, date: {}. Weather Data: {}", city, date, weatherData);
            return ResponseEntity.ok("Weather info retrieved successfully");
        } catch (Exception e) {
            log.error("Error while getting weather info. City: {}, Date: {}", city, date, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving weather info");
        }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeWeatherData(@PathVariable Long id) {
        try {
            log.info("Removing weather data with ID: {}", id);

            return ResponseEntity.ok("Data removed successfully");
        } catch (Exception e) {
            log.error("Error while removing weather data. ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error removing weather data");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateWeatherData(@PathVariable Long id, @RequestBody Map<String, Object> weatherData) {
        try {
            log.info("Updating weather data with ID: {}. New data: {}", id, weatherData);

            return ResponseEntity.ok("Data updated successfully");
        } catch (Exception e) {
            log.error("Error while updating weather data. ID: {}, New data: {}", id, weatherData, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating weather data");
        }
    }

    @GetMapping("/consume-external-api")
    public ResponseEntity<String> consumeExternalApi() {
        try {
            HttpResponse<JsonNode> response = ApiRequestUtil.fetchWeatherData("Rio de Janeiro");

            log.info("External API status code: {}", ((HttpResponse<?>) response).getStatus());


            return ResponseEntity.ok("External API consumed successfully");
        } catch (Exception e) {
            log.error("Error while consuming external API", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error consuming external API");
        }
    }
}
