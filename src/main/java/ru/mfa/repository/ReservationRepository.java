package ru.mfa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mfa.model.Reservation;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByTableId(Long tableId);
    List<Reservation> findByCustomerId(Long customerId);
    
    @Query("SELECT r FROM Reservation r WHERE r.tableId = :tableId AND r.status NOT IN ('CANCELLED', 'COMPLETED')")
    List<Reservation> findActiveReservationsByTableId(@Param("tableId") Long tableId);
}
