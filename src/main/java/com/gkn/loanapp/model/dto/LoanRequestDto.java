package com.gkn.loanapp.model.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record LoanRequestDto(@NotNull Long customerId,
                             @NotNull BigDecimal loanAmount,
                             @NotNull BigDecimal interestRate,
                             @NotNull Integer numberOfInstallments) {
}
