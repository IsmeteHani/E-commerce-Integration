package se.moln.ecommerceintegration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.moln.ecommerceintegration.client.WeatherClient;
import se.moln.ecommerceintegration.dto.weather.OpenWeatherMapResponse;
import se.moln.ecommerceintegration.dto.weather.WeatherResponse;

import java.time.LocalDateTime;


@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherClient weatherClient;

    /** Gets formatted weather information for a city*/

    public WeatherResponse getWeatherForCity(String city) {
        log.info("Fetching weather data for city: {}", city);

        // TEMPORARY: Mock data for testing (remove when API key is active)
        if ("Stockholm".equalsIgnoreCase(city) || "Prishtina".equalsIgnoreCase(city)) {
            WeatherResponse mockResponse = WeatherResponse.builder()
                    .location(city)
                    .country("SE")
                    .temperature(2.5)
                    .feelsLike(0.3)
                    .description("Light snow")
                    .humidity(85)
                    .windSpeed(4.2)
                    .timestamp(LocalDateTime.now())
                    .build();

            String formattedMessage = mockResponse.createFormattedMessage();
            mockResponse.setFormattedMessage(formattedMessage);

            log.info("✅ Returning MOCK weather data for testing: {}", city);
            return mockResponse;
        }

        // Real API call (will work when API key is activated)
        OpenWeatherMapResponse apiResponse = weatherClient.getWeatherByCity(city);
        WeatherResponse response = mapToWeatherResponse(apiResponse);

        String formattedMessage = response.createFormattedMessage();
        response.setFormattedMessage(formattedMessage);

        log.info("Successfully processed weather data for: {}", city);
        return response;
    }

    /**
     * Gets weather by coordinates
     */
    public WeatherResponse getWeatherByCoordinates(Double lat, Double lon) {
        log.info("Fetching weather data for coordinates: {}, {}", lat, lon);

        OpenWeatherMapResponse apiResponse = weatherClient.getWeatherByCoordinates(lat, lon);
        WeatherResponse response = mapToWeatherResponse(apiResponse);

        String formattedMessage = response.createFormattedMessage();
        response.setFormattedMessage(formattedMessage);

        return response;
    }

    /**
     * Maps external API response to internal DTO
     */
    private WeatherResponse mapToWeatherResponse(OpenWeatherMapResponse apiResponse) {
        String description = apiResponse.getWeather() != null && !apiResponse.getWeather().isEmpty()
                ? apiResponse.getWeather().get(0).getDescription()
                : "N/A";

        return WeatherResponse.builder()
                .location(apiResponse.getCityName())
                .country(apiResponse.getSys().getCountry())
                .temperature(apiResponse.getMain().getTemperature())
                .feelsLike(apiResponse.getMain().getFeelsLike())
                .description(capitalizeFirstLetter(description))
                .humidity(apiResponse.getMain().getHumidity())
                .windSpeed(apiResponse.getWind().getSpeed())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Helper method to capitalize first letter
     */
    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}