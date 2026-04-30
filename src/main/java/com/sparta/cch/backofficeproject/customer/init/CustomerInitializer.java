package com.sparta.cch.backofficeproject.customer.init;

import com.sparta.cch.backofficeproject.customer.entity.Customer;
import com.sparta.cch.backofficeproject.customer.entity.CustomerStatus;
import com.sparta.cch.backofficeproject.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CustomerInitializer implements CommandLineRunner {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public void run(String... args) {
        createCustomerIfNotExists("고객1", "customer01@test.com", "010-1111-1111", CustomerStatus.ACTIVE);
        createCustomerIfNotExists("고객2", "customer02@test.com", "010-2222-2222", CustomerStatus.ACTIVE);
        createCustomerIfNotExists("고객3", "customer03@test.com", "010-3333-3333", CustomerStatus.INACTIVE);
        createCustomerIfNotExists("고객4", "customer04@test.com", "010-4444-4444", CustomerStatus.SUSPENDED);
        createCustomerIfNotExists("고객5", "customer05@test.com", "010-5555-5555", CustomerStatus.ACTIVE);
        createCustomerIfNotExists("고객6", "customer06@test.com", "010-6666-6666", CustomerStatus.INACTIVE);
        createCustomerIfNotExists("고객7", "customer07@test.com", "010-7777-7777", CustomerStatus.SUSPENDED);
        createCustomerIfNotExists("고객8", "customer08@test.com", "010-8888-8888", CustomerStatus.ACTIVE);
        createCustomerIfNotExists("고객9", "customer09@test.com", "010-9999-9999", CustomerStatus.ACTIVE);
        createCustomerIfNotExists("고객10", "customer10@test.com", "010-1010-1010", CustomerStatus.SUSPENDED);
    }

    private void createCustomerIfNotExists(
            String name,
            String email,
            String phone,
            CustomerStatus status
    ) {
        if (customerRepository.existsByEmailIncludeDeleted(email) == 1) {
            return;
        }

        Customer customer = new Customer(
                name,
                email,
                phone,
                status
        );

        customerRepository.save(customer);
    }
}
