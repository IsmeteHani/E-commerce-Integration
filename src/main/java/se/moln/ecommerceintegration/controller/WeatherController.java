package se.moln.ecommerceintegration.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.moln.ecommerceintegration.dto.weather.WeatherResponse;
import se.moln.ecommerceintegration.service.WeatherService;

/* REST Controller for Weather Integration */
@Slf4j
@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
@Tag(name = "Weather Integration", description = "External API integration for weather data")
public class WeatherController {

    private final WeatherService weatherService;

    /*Get weather information for a specific city */
    @GetMapping("/city")
    @Operation(
            summary = "Get weather by city name",
            description = "Fetches current weather data for a specified city from OpenWeatherMap API"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Weather data retrieved successfully",
            content = @Content(schema = @Schema(implementation = WeatherResponse.class))
    )
    @ApiResponse(responseCode = "400", description = "Invalid city name")
    @ApiResponse(responseCode = "500", description = "External API error")
    public ResponseEntity<WeatherResponse> getWeatherByCity(
            @Parameter(description = "City name (e.g., Stockholm, London, New York)", required = true)
            @RequestParam String name
    ) {
        log.info("Received request for weather data - City: {}", name);

        if (name == null || name.trim().isEmpty()) {
            log.warn("Invalid city name provided");
            return ResponseEntity.badRequest().build();
        }

        WeatherResponse response = weatherService.getWeatherForCity(name);
        return ResponseEntity.ok(response);
    }

    /* Get weather information by geographic coordinates */
    @GetMapping("/coordinates")
    @Operation(
            summary = "Get weather by coordinates",
            description = "Fetches current weather data for specified geographic coordinates"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Weather data retrieved successfully",
            content = @Content(schema = @Schema(implementation = WeatherResponse.class))
    )
    @ApiResponse(responseCode = "400", description = "Invalid coordinates")
    @ApiResponse(responseCode = "500", description = "External API error")
    public ResponseEntity<WeatherResponse> getWeatherByCoordinates(
            @Parameter(description = "Latitude (-90 to 90)", required = true)
            @RequestParam Double lat,
            @Parameter(description = "Longitude (-180 to 180)", required = true)
            @RequestParam Double lon
    ) {
        log.info("Received request for weather data - Coordinates: {}, {}", lat, lon);

        if (lat == null || lon == null) {
            log.warn("Invalid coordinates provided");
            return ResponseEntity.badRequest().build();
        }

        if (lat < -90 || lat > 90 || lon < -180 || lon > 180) {
            log.warn("Coordinates out of valid range");
            return ResponseEntity.badRequest().build();
        }

        WeatherResponse response = weatherService.getWeatherByCoordinates(lat, lon);
        return ResponseEntity.ok(response);
    }

    /* Health check endpoint for weather service */
    @GetMapping("/health")
    @Operation(
            summary = "Weather service health check",
            description = "Check if weather integration service is running"
    )
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Weather Integration Service is running ✅");
    }
}