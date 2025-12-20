package ru.mfa.model;

import lombok.Data;

@Data
public class Restaurant {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String openingHours;
    private String description;
}
