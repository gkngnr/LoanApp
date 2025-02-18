package com.example.demo.service;

import com.example.demo.model.dto.LoanInstallmentResponseDto;
import com.example.demo.model.entity.Loan;
import com.example.demo.model.entity.LoanInstallment;
import com.example.demo.model.mapper.LoanInstallmentMapper;
import com.example.demo.repository.LoanInstallmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoanInstallmentService {

    private final LoanInstallmentRepository loanInstallmentRepository;

    @Transactional
    public void generateInstallmentsFromLoan(Loan loan) {
        final var installmentAmount = loan.getLoanAmount()
                .divide(BigDecimal.valueOf(loan.getNumberOfInstallments()), RoundingMode.HALF_UP);
        List<LoanInstallment> installments = new ArrayList<>();
        var dueDate = LocalDate.now().plusMonths(1).withDayOfMonth(1);

        for (int i = 0; i < loan.getNumberOfInstallments(); i++) {
            var installment = createSingleLoanInstallment(loan, installmentAmount, dueDate);
            installments.add(installment);
            dueDate = dueDate.plusMonths(1);
        }

        loanInstallmentRepository.saveAll(installments);
    }

    @Transactional
    public void saveAllLoanInstallments(List<LoanInstallment> installments) {
        loanInstallmentRepository.saveAll(installments);
    }

    public List<LoanInstallmentResponseDto> getLoanInstallmentsByLoanId(Long loanId) {
        return loanInstallmentRepository.findByLoanIdOrderByDueDateAsc(loanId)
                .stream()
                .map(LoanInstallmentMapper::toLoanInstallmentDto).toList();
    }

    public List<LoanInstallment> getUnpaidLoanInstallments(Long loanId) {
        return loanInstallmentRepository.findByLoanIdAndIsPaidFalseOrderByDueDateAsc(loanId);
    }

    public boolean checkIfInstallmentsArePaid(Long loanId) {
        return loanInstallmentRepository.countByLoanIdAndIsPaidFalse(loanId) == 0;
    }

    private LoanInstallment createSingleLoanInstallment(Loan loan, BigDecimal installmentAmount, LocalDate dueDate) {
        return LoanInstallment.builder()
                .loan(loan)
                .amount(installmentAmount)
                .paidAmount(BigDecimal.ZERO)
                .dueDate(dueDate)
                .isPaid(false)
                .build();
    }

}
