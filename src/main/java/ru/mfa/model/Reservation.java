package ru.mfa.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Reservation {
    private Long id;
    private Long customerId;
    private Long tableId;
    private LocalDateTime reservationTime;
    private Integer durationHours; // продолжительность в часах
    private Integer numberOfGuests;
    private String status; // "PENDING", "CONFIRMED", "CANCELLED", "COMPLETED"
    private String specialRequests;
    private LocalDateTime createdAt;
}
