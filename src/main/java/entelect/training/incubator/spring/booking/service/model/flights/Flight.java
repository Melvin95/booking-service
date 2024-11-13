package entelect.training.incubator.spring.booking.service.model.flights;

import jakarta.persistence.Entity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * {
 *     "id": 1,
 *     "flightNumber": "MN123",
 *     "origin": "JOHANNESBURG",
 *     "destination": "CAPE_TOWN",
 *     "departureTime": "2020-05-06T12:35:00",
 *     "arrivalTime": "2020-05-06T14:40:00",
 *     "seatsAvailable": 192,
 *     "seatCost": 895.0
 * }
 */
@Data
public class Flight {
    private Integer id;
    private String flightNumber;
    private String origin;
    private String destination;
    private String departureTime;
    private String arrivalTime;
    private Integer seatsAvailable;
    private BigDecimal seatCost;
}
