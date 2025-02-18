package com.gkn.loanapp.service;

import com.gkn.loanapp.model.dto.CustomerDto;
import com.gkn.loanapp.model.entity.Customer;
import com.gkn.loanapp.model.exception.CustomerNotFoundException;
import com.gkn.loanapp.model.mapper.CustomerMapper;
import com.gkn.loanapp.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerDto getCustomerById(Long id) {
        return customerRepository.findById(id).map(CustomerMapper::toCustomerDto)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
    }

    public Customer getCustomerEntityById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
    }

    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream().map(CustomerMapper::toCustomerDto).toList();
    }

    @Transactional
    public CustomerDto saveCustomer(CustomerDto customerDto) {
        return CustomerMapper.toCustomerDto(customerRepository.save(CustomerMapper.toCustomer(customerDto)));
    }

    @Transactional
    public void updateCustomerUsedCreditLimit(Customer customer, BigDecimal loanAmount) {
        customer.setUsedCreditLimit(customer.getUsedCreditLimit().add(loanAmount));
    }

}
