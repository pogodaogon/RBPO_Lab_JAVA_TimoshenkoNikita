package ru.mfa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mfa.model.Table;
import ru.mfa.repository.TableRepository;
import java.util.List;
import java.util.Optional;

@Service
public class TableService {

    @Autowired
    private TableRepository tableRepository;

    public Table createTable(Table table) {
        return tableRepository.save(table);
    }

    public List<Table> getAllTables() {
        return tableRepository.findAll();
    }

    public Optional<Table> getTableById(Long id) {
        return tableRepository.findById(id);
    }

    public List<Table> getTablesByRestaurantId(Long restaurantId) {
        return tableRepository.findByRestaurantId(restaurantId);
    }

    public Table updateTable(Long id, Table tableDetails) {
        Table table = tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Столик не найден с id: " + id));

        if (tableDetails.getTableNumber() != null) table.setTableNumber(tableDetails.getTableNumber());
        if (tableDetails.getCapacity() != null) table.setCapacity(tableDetails.getCapacity());
        if (tableDetails.getLocation() != null) table.setLocation(tableDetails.getLocation());
        if (tableDetails.getIsAvailable() != null) table.setIsAvailable(tableDetails.getIsAvailable());

        return tableRepository.save(table);
    }

    public void deleteTable(Long id) {
        tableRepository.deleteById(id);
    }
}
