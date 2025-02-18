package com.gkn.loanapp;

import com.gkn.loanapp.model.entity.Customer;
import com.gkn.loanapp.repository.CustomerRepository;
import com.gkn.loanapp.security.User;
import com.gkn.loanapp.security.UserRepository;
import com.gkn.loanapp.security.enums.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

@SpringBootApplication
public class LoanAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoanAppApplication.class, args);
    }

    @Profile("!prd")
    @Bean
    CommandLineRunner init(UserRepository userRepo,
                           PasswordEncoder passwordEncoder,
                           CustomerRepository customerRepository) {
        return args -> {
            if (userRepo.findByUsername("admin").isEmpty()) {
                var admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("adminx"))
                        .role(Role.ADMIN)
                        .build();
                var user1 = User.builder()
                        .username("john.doe")
                        .password(passwordEncoder.encode("john.doex"))
                        .role(Role.CUSTOMER)
                        .build();
                var user2 = User.builder()
                        .username("jane.doe")
                        .password(passwordEncoder.encode("jane.doex"))
                        .role(Role.CUSTOMER)
                        .build();

                var customer1 = Customer.builder()
                        .username("john.doe")
                        .name("John")
                        .surname("Doe")
                        .usedCreditLimit(BigDecimal.valueOf(1000.0))
                        .build();
                var customer2 = Customer.builder()
                        .username("jane.doe")
                        .name("Jane")
                        .surname("Doe")
                        .usedCreditLimit(BigDecimal.valueOf(2000.0))
                        .build();

                customerRepository.saveAll(List.of(customer1, customer2));
                userRepo.saveAll(List.of(admin, user1, user2));
            }
        };
    }

}
