package com.example.demo.service;

import com.example.demo.model.dto.LoanPaymentRequestDto;
import com.example.demo.model.dto.LoanPaymentResponseDto;
import com.example.demo.model.entity.LoanInstallment;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanPaymentService {

    private final LoanService loanService;
    private final LoanInstallmentService loanInstallmentService;

    @Value("${loan.penaltyRate}")
    private Double penaltyRate;


    @Transactional
    public LoanPaymentResponseDto payLoan(LoanPaymentRequestDto requestDto) {
        final var loanId = requestDto.loanId();
        List<LoanInstallment> unpaidInstallments = loanInstallmentService.getUnpaidLoanInstallments(loanId);

        final var now = LocalDate.now();
        final var maxPayableDate = now.plusMonths(3);
        BigDecimal remainingAmount = requestDto.amount();
        int installmentsPaid = 0;
        BigDecimal totalSpent = BigDecimal.ZERO;


        for (LoanInstallment installment : unpaidInstallments) {
            if (installment.getDueDate().isAfter(maxPayableDate)) {
                break;
            }
            BigDecimal finalPayment = applyPenaltyOrReward(installment);

            if (remainingAmount.compareTo(finalPayment) <= 0) {
                break;
            }

            remainingAmount = remainingAmount.subtract(finalPayment);
            totalSpent = totalSpent.add(finalPayment);

            installment.setPaidAmount(finalPayment);
            installment.setPaymentDate(now);
            installment.setPaid(true);

            installmentsPaid++;
        }

        loanInstallmentService.saveAllLoanInstallments(unpaidInstallments);

        boolean isLoanFullyPaid = loanInstallmentService.checkIfInstallmentsArePaid(loanId);
        if (isLoanFullyPaid) {
            loanService.updateLoanAsPaid(loanId);
        }
        return LoanPaymentResponseDto.builder()
                .installmentsPaid(installmentsPaid)
                .totalAmountSpent(totalSpent)
                .isLoanFullyPaid(isLoanFullyPaid)
                .build();

    }

    private BigDecimal applyPenaltyOrReward(LoanInstallment installment) {
        final var dayDiff = ChronoUnit.DAYS.between(installment.getDueDate(), LocalDate.now());
        var penalty = installment.getAmount()
                .multiply(BigDecimal.valueOf(penaltyRate))
                .multiply(BigDecimal.valueOf(dayDiff));
        return installment.getAmount().add(penalty);
    }

}
