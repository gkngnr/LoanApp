package com.gkn.loanapp.repository;

import com.gkn.loanapp.model.entity.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanInstallmentRepository extends JpaRepository<LoanInstallment, Long> {
    int countByLoanIdAndIsPaidFalse(Long loanId);
    List<LoanInstallment> findByLoanIdAndIsPaidFalseOrderByDueDateAsc(Long loanId);
    List<LoanInstallment> findByLoanIdOrderByDueDateAsc(Long loanId);
}
