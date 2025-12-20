package ru.mfa.repository;

import org.springframework.stereotype.Repository;
import ru.mfa.model.MenuItem;
import java.util.*;

@Repository
public class MenuItemRepository {
    private final Map<Long, MenuItem> menuItems = new HashMap<>();
    private Long nextId = 1L;

    public MenuItem save(MenuItem menuItem) {
        if (menuItem.getId() == null) {
            menuItem.setId(nextId++);
        }
        menuItems.put(menuItem.getId(), menuItem);
        return menuItem;
    }

    public Optional<MenuItem> findById(Long id) {
        return Optional.ofNullable(menuItems.get(id));
    }

    public List<MenuItem> findAll() {
        return new ArrayList<>(menuItems.values());
    }

    public List<MenuItem> findByRestaurantId(Long restaurantId) {
        return menuItems.values().stream()
                .filter(item -> restaurantId.equals(item.getRestaurantId()))
                .toList();
    }

    public void deleteById(Long id) {
        menuItems.remove(id);
    }
}
