package com.gkn.loanapp.model.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record LoanInstallmentResponseDto(Long id,
                                         BigDecimal amount,
                                         BigDecimal paidAmount,
                                         LocalDate dueDate,
                                         LocalDate paymentDate,
                                         boolean isPaid) {
}
