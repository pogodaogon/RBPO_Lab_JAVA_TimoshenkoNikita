package ru.mfa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mfa.model.Restaurant;
import ru.mfa.repository.RestaurantRepository;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    public Restaurant createRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Optional<Restaurant> getRestaurantById(Long id) {
        return restaurantRepository.findById(id);
    }

    public Restaurant updateRestaurant(Long id, Restaurant restaurantDetails) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ресторан не найден с id: " + id));

        if (restaurantDetails.getName() != null) restaurant.setName(restaurantDetails.getName());
        if (restaurantDetails.getAddress() != null) restaurant.setAddress(restaurantDetails.getAddress());
        if (restaurantDetails.getPhone() != null) restaurant.setPhone(restaurantDetails.getPhone());
        if (restaurantDetails.getOpeningHours() != null) restaurant.setOpeningHours(restaurantDetails.getOpeningHours());
        if (restaurantDetails.getDescription() != null) restaurant.setDescription(restaurantDetails.getDescription());

        return restaurantRepository.save(restaurant);
    }

    public void deleteRestaurant(Long id) {
        restaurantRepository.deleteById(id);
    }
}
