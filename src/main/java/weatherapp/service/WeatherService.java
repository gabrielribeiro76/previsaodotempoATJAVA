package weatherapp.service;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import weatherapp.model.WeatherData;
import weatherapp.util.ApiRequestUtil;

@Service
@Slf4j
public class WeatherService {

    private static final String DEFAULT_CITY = "Rio";
    private static final String DEFAULT_DATE = "2023-12-01";

    public WeatherData getWeatherData(String city, String date) {
        if (city == null) {
            city = DEFAULT_CITY;
        }

        if (date == null) {
            date = DEFAULT_DATE;
        }

        HttpResponse<JsonNode> response = ApiRequestUtil.fetchWeatherData(city);
        JsonNode body = response.getBody();
        WeatherData weatherData = new WeatherData();
        weatherData.setCity(city);

        if (body != null && body.getObject().has("weather")) {
            String description = body.getObject()
                    .getJSONArray("weather")
                    .getJSONObject(0)
                    .getString("description");
            weatherData.setDescription(description);
        } else {
            weatherData.setDescription("Descrição não disponível");
        }

        if (body != null && body.getObject().has("main")) {
            if (body.getObject().getJSONObject("main").has("temp")) {
                String temperatureKelvinStr = body.getObject()
                        .getJSONObject("main")
                        .getString("temp");

                temperatureKelvinStr = temperatureKelvinStr.replace(",", ".");

                double temperatureKelvin = Double.parseDouble(temperatureKelvinStr);
                double temperatureCelsius = temperatureKelvin - 273.15;

                weatherData.setTemperature(temperatureCelsius);
            } else {
                weatherData.setTemperature(Double.NaN);
            }
        } else {
            weatherData.setTemperature(Double.NaN);
        }

        int statusCode = response.getStatus();
        log.info("External API status code: {}", statusCode);

        return weatherData;
    }

    public double getTemperature(String testCity) {
        return 0;
    }
}