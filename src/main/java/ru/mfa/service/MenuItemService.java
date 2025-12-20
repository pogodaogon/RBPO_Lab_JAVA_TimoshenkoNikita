package ru.mfa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mfa.model.MenuItem;
import ru.mfa.repository.MenuItemRepository;
import java.util.List;
import java.util.Optional;

@Service
public class MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    public MenuItem createMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    public Optional<MenuItem> getMenuItemById(Long id) {
        return menuItemRepository.findById(id);
    }

    public List<MenuItem> getMenuItemsByRestaurantId(Long restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId);
    }

    public MenuItem updateMenuItem(Long id, MenuItem menuItemDetails) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пункт меню не найден с id: " + id));

        if (menuItemDetails.getName() != null) menuItem.setName(menuItemDetails.getName());
        if (menuItemDetails.getDescription() != null) menuItem.setDescription(menuItemDetails.getDescription());
        if (menuItemDetails.getCategory() != null) menuItem.setCategory(menuItemDetails.getCategory());
        if (menuItemDetails.getPrice() != null) menuItem.setPrice(menuItemDetails.getPrice());
        if (menuItemDetails.getIsAvailable() != null) menuItem.setIsAvailable(menuItemDetails.getIsAvailable());

        return menuItemRepository.save(menuItem);
    }

    public void deleteMenuItem(Long id) {
        menuItemRepository.deleteById(id);
    }
}
