package ru.mfa.repository;

import org.springframework.stereotype.Repository;
import ru.mfa.model.Reservation;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class ReservationRepository {
    private final Map<Long, Reservation> reservations = new HashMap<>();
    private Long nextId = 1L;

    public Reservation save(Reservation reservation) {
        if (reservation.getId() == null) {
            reservation.setId(nextId++);
        }
        reservations.put(reservation.getId(), reservation);
        return reservation;
    }

    public Optional<Reservation> findById(Long id) {
        return Optional.ofNullable(reservations.get(id));
    }

    public List<Reservation> findAll() {
        return new ArrayList<>(reservations.values());
    }

    public void deleteById(Long id) {
        reservations.remove(id);
    }

    public List<Reservation> findByTableId(Long tableId) {
        return reservations.values().stream()
                .filter(reservation -> tableId.equals(reservation.getTableId()))
                .toList();
    }

    public List<Reservation> findByCustomerId(Long customerId) {
        return reservations.values().stream()
                .filter(reservation -> customerId.equals(reservation.getCustomerId()))
                .toList();
    }

    public List<Reservation> findOverlappingReservations(Long tableId, LocalDateTime startTime, LocalDateTime endTime) {
        return reservations.values().stream()
                .filter(reservation -> tableId.equals(reservation.getTableId()))
                .filter(reservation -> {
                    LocalDateTime resStart = reservation.getReservationTime();
                    LocalDateTime resEnd = resStart.plusHours(reservation.getDurationHours());
                    return (startTime.isBefore(resEnd) && endTime.isAfter(resStart));
                })
                .toList();
    }
}
