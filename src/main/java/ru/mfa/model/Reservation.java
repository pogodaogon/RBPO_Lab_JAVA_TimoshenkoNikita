package ru.mfa.model;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;

@Data
@Entity
@jakarta.persistence.Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "customer_id", nullable = false)
    private Long customerId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    @JsonIgnore
    private Customer customer;
    
    @Column(name = "table_id", nullable = false)
    private Long tableId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", insertable = false, updatable = false)
    @JsonIgnore
    private Table tableItem;
    
    @Column(nullable = false)
    private LocalDateTime reservationTime;
    
    @Column(nullable = false)
    private Integer durationHours; // продолжительность в часах
    
    @Column(nullable = false)
    private Integer numberOfGuests;
    
    @Column(nullable = false)
    private String status = "PENDING"; // "PENDING", "CONFIRMED", "CANCELLED", "COMPLETED"
    
    private String specialRequests;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
