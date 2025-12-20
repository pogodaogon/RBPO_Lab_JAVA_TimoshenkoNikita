package ru.mfa.repository;

import org.springframework.stereotype.Repository;
import ru.mfa.model.Table;
import java.util.*;

@Repository
public class TableRepository {
    private final Map<Long, Table> tables = new HashMap<>();
    private Long nextId = 1L;

    public Table save(Table table) {
        if (table.getId() == null) {
            table.setId(nextId++);
        }
        tables.put(table.getId(), table);
        return table;
    }

    public Optional<Table> findById(Long id) {
        return Optional.ofNullable(tables.get(id));
    }

    public List<Table> findAll() {
        return new ArrayList<>(tables.values());
    }

    public List<Table> findByRestaurantId(Long restaurantId) {
        return tables.values().stream()
                .filter(table -> restaurantId.equals(table.getRestaurantId()))
                .toList();
    }

    public void deleteById(Long id) {
        tables.remove(id);
    }
}
