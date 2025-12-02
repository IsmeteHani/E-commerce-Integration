package se.moln.ecommerceintegration.dto.weather;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse {

    private String location;
    private String country;
    private Double temperature;
    private Double feelsLike;
    private String description;
    private Integer humidity;
    private Double windSpeed;
    private String formattedMessage;
    private LocalDateTime timestamp;

    public String createFormattedMessage() {
        return String.format(
                "🌤️ Weather Report for %s, %s\n" +
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                        "🌡️  Temperature: %.1f°C (feels like %.1f°C)\n" +
                        "☁️  Conditions: %s\n" +
                        "💧 Humidity: %d%%\n" +
                        "💨 Wind Speed: %.1f m/s\n" +
                        "⏰ Updated: %s\n" +
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━",
                location, country, temperature, feelsLike,
                description, humidity, windSpeed, timestamp
        );
    }
}
