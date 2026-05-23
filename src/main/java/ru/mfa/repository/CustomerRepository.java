package ru.mfa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mfa.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
