package com.gkn.loanapp.model.mapper;

import com.gkn.loanapp.model.dto.LoanResponseDto;
import com.gkn.loanapp.model.entity.Loan;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class LoanMapper {

    public static LoanResponseDto toLoanResponseDto(Loan loan) {
        return LoanResponseDto.builder()
                .id(loan.getId())
                .customerId(loan.getCustomer().getId())
                .loanAmount(loan.getLoanAmount())
                .numberOfInstallments(loan.getNumberOfInstallments())
                .createDate(loan.getCreateDate())
                .isPaid(loan.isPaid())
                .interestRate(loan.getInterestRate())
                .build();
    }

}
