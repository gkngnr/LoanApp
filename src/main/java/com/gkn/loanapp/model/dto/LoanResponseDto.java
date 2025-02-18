package com.example.demo.model.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record LoanResponseDto(Long id,
                              Long customerId,
                              BigDecimal loanAmount,
                              int numberOfInstallments,
                              BigDecimal interestRate,
                              LocalDate createDate,
                              boolean isPaid) {
}
