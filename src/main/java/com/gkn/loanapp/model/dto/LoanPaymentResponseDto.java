package com.gkn.loanapp.model.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record LoanPaymentResponseDto(int installmentsPaid,
                                     BigDecimal totalAmountSpent,
                                     boolean isLoanFullyPaid) {
}
