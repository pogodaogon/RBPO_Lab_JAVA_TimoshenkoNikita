package ru.mfa.repository;

import org.springframework.stereotype.Repository;
import ru.mfa.model.Restaurant;
import java.util.*;

@Repository
public class RestaurantRepository {
    private final Map<Long, Restaurant> restaurants = new HashMap<>();
    private Long nextId = 1L;

    public Restaurant save(Restaurant restaurant) {
        if (restaurant.getId() == null) {
            restaurant.setId(nextId++);
        }
        restaurants.put(restaurant.getId(), restaurant);
        return restaurant;
    }

    public Optional<Restaurant> findById(Long id) {
        return Optional.ofNullable(restaurants.get(id));
    }

    public List<Restaurant> findAll() {
        return new ArrayList<>(restaurants.values());
    }

    public void deleteById(Long id) {
        restaurants.remove(id);
    }
}
