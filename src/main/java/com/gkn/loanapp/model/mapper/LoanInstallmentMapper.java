package com.gkn.loanapp.model.mapper;

import com.gkn.loanapp.model.dto.LoanInstallmentResponseDto;
import com.gkn.loanapp.model.entity.LoanInstallment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class LoanInstallmentMapper {

    public static LoanInstallmentResponseDto toLoanInstallmentDto(LoanInstallment loanInstallment) {
        return LoanInstallmentResponseDto.builder()
                .id(loanInstallment.getId())
                .amount(loanInstallment.getAmount())
                .paidAmount(loanInstallment.getPaidAmount())
                .dueDate(loanInstallment.getDueDate())
                .paymentDate(loanInstallment.getPaymentDate())
                .isPaid(loanInstallment.isPaid())
                .build();
    }
}
