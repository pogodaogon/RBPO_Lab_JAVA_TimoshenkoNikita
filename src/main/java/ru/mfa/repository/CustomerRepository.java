package ru.mfa.repository;

import org.springframework.stereotype.Repository;
import ru.mfa.model.Customer;
import java.util.*;

@Repository
public class CustomerRepository {
    private final Map<Long, Customer> customers = new HashMap<>();
    private Long nextId = 1L;

    public Customer save(Customer customer) {
        if (customer.getId() == null) {
            customer.setId(nextId++);
        }
        customers.put(customer.getId(), customer);
        return customer;
    }

    public Optional<Customer> findById(Long id) {
        return Optional.ofNullable(customers.get(id));
    }

    public List<Customer> findAll() {
        return new ArrayList<>(customers.values());
    }

    public void deleteById(Long id) {
        customers.remove(id);
    }

    public boolean existsById(Long id) {
        return customers.containsKey(id);
    }
}
