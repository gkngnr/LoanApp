package com.example.demo.controller;

import com.example.demo.model.dto.CustomerDto;
import com.example.demo.security.annotations.HasAdminRole;
import com.example.demo.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@HasAdminRole
public class CustomerController {

    private final CustomerService customerService;

    @Operation(summary = "List all loans", description = "Retrieve a list of loans")
    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.getCustomerById(customerId));
    }

    @PostMapping
    public ResponseEntity<CustomerDto> saveCustomer(@RequestBody CustomerDto customerDto, UriComponentsBuilder uriComponentsBuilder) {
        final var createdCustomer = customerService.saveCustomer(customerDto);
        final var uriComponents = uriComponentsBuilder
                .path("/api/v1/customers/{id}")
                .buildAndExpand(createdCustomer.id());
        final var httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponents.toUri());
        return new ResponseEntity<>(createdCustomer, httpHeaders, HttpStatus.CREATED);
    }
}
