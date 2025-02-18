package com.gkn.loanapp.model.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CustomerDto(Long id,
                          String name,
                          String surname,
                          String username,
                          BigDecimal creditLimit,
                          BigDecimal usedCreditLimit) {
}
