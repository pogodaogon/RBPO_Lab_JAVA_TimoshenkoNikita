package ru.mfa.model;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;

@Data
@Entity
@jakarta.persistence.Table(name = "menu_items")
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", insertable = false, updatable = false)
    @JsonIgnore
    private Restaurant restaurant;
    
    @Column(nullable = false)
    private String name;
    
    @Column(length = 1000)
    private String description;
    
    private String category; // "закуски", "основные блюда", "десерты", "напитки"
    
    @Column(nullable = false)
    private BigDecimal price;
    
    @Column(nullable = false)
    private Boolean isAvailable = true;
}
