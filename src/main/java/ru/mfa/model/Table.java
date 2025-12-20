package ru.mfa.model;

import lombok.Data;

@Data
public class Table {
    private Long id;
    private Long restaurantId;
    private String tableNumber;
    private Integer capacity;
    private String location; // "у окна", "в зале", "на террасе", "VIP"
    private Boolean isAvailable;
}