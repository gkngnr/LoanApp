package com.gkn.loanapp.service.validator;

import com.gkn.loanapp.model.dto.LoanRequestDto;
import com.gkn.loanapp.model.entity.Customer;
import com.gkn.loanapp.model.exception.LoanValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class LoanCreateValidator {

    @Value("#{'${loan.installments}'.split(',')}")
    private List<Integer> allowedInstallments;
    @Value("${loan.minInterestRate}")
    private BigDecimal minInterestRate;
    @Value("${loan.maxInterestRate}")
    private BigDecimal maxInterestRate;

    public void validateLoanRequest(Customer customer, LoanRequestDto loanRequestDto) {
        validateCustomerLimit(customer, loanRequestDto.loanAmount());
        validateInstallments(loanRequestDto.numberOfInstallments());
        validateInterestRate(loanRequestDto.interestRate());
    }

    private void validateCustomerLimit(Customer customer, BigDecimal amount) {
        BigDecimal availableLimit = customer.getCreditLimit().subtract(customer.getUsedCreditLimit());
        if (availableLimit.compareTo(amount) < 0) {
            throw new LoanValidationException("Insufficient credit limit for this loan.");
        }
    }

    private void validateInstallments(int installments) {
        if (!allowedInstallments.contains(installments)) {
            throw new LoanValidationException("Allowed number of installments: " + allowedInstallments);
        }
    }

    private void validateInterestRate(BigDecimal interestRate) {
        if (interestRate.compareTo(minInterestRate) < 0 || interestRate.compareTo(maxInterestRate) > 0) {
            throw new LoanValidationException("Interest rate must be between " + minInterestRate + " and " + maxInterestRate);
        }
    }
}
