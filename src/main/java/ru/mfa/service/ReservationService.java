package ru.mfa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mfa.model.Reservation;
import ru.mfa.model.Table;
import ru.mfa.repository.ReservationRepository;
import ru.mfa.repository.TableRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private CustomerService customerService;

    public Reservation createReservation(Reservation reservation) {
        // Проверка существования клиента
        if (!customerService.customerExists(reservation.getCustomerId())) {
            throw new RuntimeException("Клиент с id " + reservation.getCustomerId() + " не существует");
        }

        // Проверка существования столика
        Optional<Table> tableOpt = tableRepository.findById(reservation.getTableId());
        if (tableOpt.isEmpty()) {
            throw new RuntimeException("Столик с id " + reservation.getTableId() + " не существует");
        }

        Table table = tableOpt.get();

        // Проверка доступности столика
        if (Boolean.FALSE.equals(table.getIsAvailable())) {
            throw new RuntimeException("Столик " + table.getTableNumber() + " недоступен для бронирования");
        }

        // Проверка вместимости
        if (reservation.getNumberOfGuests() > table.getCapacity()) {
            throw new RuntimeException("Столик вмещает только " + table.getCapacity() + " человек");
        }

        // Проверка на пересечение бронирований
        LocalDateTime startTime = reservation.getReservationTime();
        LocalDateTime endTime = startTime.plusHours(reservation.getDurationHours());

        List<Reservation> overlapping = reservationRepository.findOverlappingReservations(
                reservation.getTableId(), startTime, endTime);

        if (!overlapping.isEmpty()) {
            throw new RuntimeException("Столик уже забронирован на это время");
        }

        // Установка статуса и времени создания
        reservation.setStatus("CONFIRMED");
        reservation.setCreatedAt(LocalDateTime.now());

        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    public List<Reservation> getReservationsByCustomerId(Long customerId) {
        return reservationRepository.findByCustomerId(customerId);
    }

    public List<Reservation> getReservationsByTableId(Long tableId) {
        return reservationRepository.findByTableId(tableId);
    }

    public Reservation updateReservation(Long id, Reservation reservationDetails) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Бронь не найдена с id: " + id));

        // Можно обновлять только определенные поля
        if (reservationDetails.getNumberOfGuests() != null) {
            reservation.setNumberOfGuests(reservationDetails.getNumberOfGuests());
        }
        if (reservationDetails.getSpecialRequests() != null) {
            reservation.setSpecialRequests(reservationDetails.getSpecialRequests());
        }
        if (reservationDetails.getStatus() != null) {
            reservation.setStatus(reservationDetails.getStatus());
        }

        return reservationRepository.save(reservation);
    }

    public void cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Бронь не найдена с id: " + id));

        reservation.setStatus("CANCELLED");
        reservationRepository.save(reservation);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<Table> findAvailableTables(LocalDateTime startTime, int durationHours, int guests) {
        LocalDateTime endTime = startTime.plusHours(durationHours);

        return tableRepository.findAll().stream()
                .filter(table -> Boolean.TRUE.equals(table.getIsAvailable()))
                .filter(table -> table.getCapacity() >= guests)
                .filter(table -> reservationRepository.findOverlappingReservations(
                        table.getId(), startTime, endTime).isEmpty())
                .toList();
    }
}
