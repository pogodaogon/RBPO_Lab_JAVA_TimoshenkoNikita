package ru.mfa.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MenuItem {
    private Long id;
    private Long restaurantId;
    private String name;
    private String description;
    private String category; // "закуски", "основные блюда", "десерты", "напитки"
    private BigDecimal price;
    private Boolean isAvailable;
}
