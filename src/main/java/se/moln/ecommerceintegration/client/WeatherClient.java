package se.moln.ecommerceintegration.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import se.moln.ecommerceintegration.dto.weather.OpenWeatherMapResponse;

/**
 * Client for communicating with OpenWeatherMap API
 * Handles HTTP requests to external weather service
 */
@Slf4j
@Component
public class WeatherClient {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String apiUrl;

    public WeatherClient(
            RestTemplate restTemplate,
            @Value("${weather.api.key:demo}") String apiKey,
            @Value("${weather.api.url:https://api.openweathermap.org/data/2.5/weather}") String apiUrl
    ) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
    }

    /**
     * Fetches weather data for a given city
     */
    public OpenWeatherMapResponse getWeatherByCity(String city) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("q", city)
                    .queryParam("appid", apiKey)
                    .queryParam("units", "metric")
                    .toUriString();

            log.info("Calling OpenWeatherMap API for city: {}", city);

            OpenWeatherMapResponse response = restTemplate.getForObject(url, OpenWeatherMapResponse.class);

            log.info("Successfully retrieved weather data for: {}", city);
            return response;

        } catch (Exception e) {
            log.error("Failed to fetch weather data for city: {}", city, e);
            throw new RuntimeException("Failed to fetch weather data: " + e.getMessage(), e);
        }
    }

    /**
     * Fetches weather data by coordinates
     */
    public OpenWeatherMapResponse getWeatherByCoordinates(Double lat, Double lon) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("lat", lat)
                    .queryParam("lon", lon)
                    .queryParam("appid", apiKey)
                    .queryParam("units", "metric")
                    .toUriString();

            log.info("Calling OpenWeatherMap API for coordinates: {}, {}", lat, lon);

            return restTemplate.getForObject(url, OpenWeatherMapResponse.class);

        } catch (Exception e) {
            log.error("Failed to fetch weather data for coordinates: {}, {}", lat, lon, e);
            throw new RuntimeException("Failed to fetch weather data: " + e.getMessage(), e);
        }
    }
}