package com.gkn.loanapp.repository;

import com.gkn.loanapp.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
