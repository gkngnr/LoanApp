package com.example.demo.service;

import com.example.demo.model.dto.CustomerDto;
import com.example.demo.model.entity.Customer;
import com.example.demo.model.exception.CustomerNotFoundException;
import com.example.demo.model.mapper.CustomerMapper;
import com.example.demo.repository.CustomerRepository;
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
