package com.example.demo.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record LoanPaymentRequestDto(@NotNull Long loanId,
                                    @NotNull BigDecimal amount) {
}
