package com.gkn.loanapp.model.mapper;

import com.gkn.loanapp.model.dto.CustomerDto;
import com.gkn.loanapp.model.entity.Customer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class CustomerMapper {

    public static CustomerDto toCustomerDto(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .surname(customer.getSurname())
                .username(customer.getUsername())
                .creditLimit(customer.getCreditLimit())
                .usedCreditLimit(customer.getUsedCreditLimit())
                .build();
    }

    public static Customer toCustomer(CustomerDto customerDto) {
        return Customer.builder()
                .name(customerDto.name())
                .surname(customerDto.surname())
                .username(customerDto.username())
                .creditLimit(customerDto.creditLimit())
                .usedCreditLimit(customerDto.usedCreditLimit())
                .build();
    }
}
