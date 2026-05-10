package ru.mfa;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.mfa.model.Customer;
import ru.mfa.model.Restaurant;
import ru.mfa.model.Table;
import ru.mfa.repository.CustomerRepository;
import ru.mfa.repository.RestaurantRepository;
import ru.mfa.repository.TableRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final TableRepository tableRepository;

    @Override
    public void run(String... args) {
        if (restaurantRepository.count() == 0) {
            Restaurant rest = new Restaurant();
            rest.setName("La Bella Italia");
            rest.setAddress("ул. Пушкинская, д. 15");
            rest.setPhone("+74951234567");
            rest.setOpeningHours("12:00-23:00");
            rest.setDescription("Ресторан итальянской кухни");
            restaurantRepository.save(rest);

            Table table1 = new Table();
            table1.setRestaurantId(rest.getId());
            table1.setTableNumber("A1");
            table1.setCapacity(4);
            table1.setLocation("у окна");
            table1.setIsAvailable(true);
            tableRepository.save(table1);

            Table table2 = new Table();
            table2.setRestaurantId(rest.getId());
            table2.setTableNumber("VIP-1");
            table2.setCapacity(2);
            table2.setLocation("VIP");
            table2.setIsAvailable(true);
            tableRepository.save(table2);
        }

        if (customerRepository.count() == 0) {
            Customer c1 = new Customer();
            c1.setName("Алексей Петров");
            c1.setEmail("alexey@example.com");
            c1.setPhone("+79161234567");
            customerRepository.save(c1);
        }
    }
}
