package com.gkn.loanapp.service;

import com.gkn.loanapp.model.dto.LoanInstallmentResponseDto;
import com.gkn.loanapp.model.entity.Loan;
import com.gkn.loanapp.model.entity.LoanInstallment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanInstallmentServiceTest {

    @InjectMocks
    private LoanInstallmentService loanInstallmentService;

    @Mock
    private com.gkn.loanapp.repository.LoanInstallmentRepository loanInstallmentRepository;

    @Test
    void shouldReturnInstallmentsByLoanIdOrderedByDueDate() {
        Long loanId = 1L;
        List<LoanInstallment> installments = List.of(
                new LoanInstallment(1L, null, BigDecimal.valueOf(100), BigDecimal.ZERO, LocalDate.of(2024, 3, 1), null, false),
                new LoanInstallment(2L, null, BigDecimal.valueOf(100), BigDecimal.ZERO, LocalDate.of(2024, 4, 1), null, false)
        );
        when(loanInstallmentRepository.findByLoanIdOrderByDueDateAsc(loanId)).thenReturn(installments);

        List<LoanInstallmentResponseDto> result = loanInstallmentService.getLoanInstallmentsByLoanId(loanId);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).dueDate()).isEqualTo(LocalDate.of(2024, 3, 1));
        assertThat(result.get(1).dueDate()).isEqualTo(LocalDate.of(2024, 4, 1));
    }

    @Test
    void shouldReturnEmptyListIfNoInstallmentsExist() {
        when(loanInstallmentRepository.findByLoanIdOrderByDueDateAsc(1L)).thenReturn(Collections.emptyList());
        assertThat(loanInstallmentService.getLoanInstallmentsByLoanId(1L)).isEmpty();
    }

    @Test
    void shouldGenerateInstallmentsForLoan() {
        Loan loan = Loan.builder()
                .id(1L)
                .interestRate(BigDecimal.valueOf(0.1))
                .loanAmount(BigDecimal.valueOf(1200))
                .numberOfInstallments(12)
                .createDate(LocalDate.now())
                .isPaid(false)
                .build();
        loanInstallmentService.generateInstallmentsFromLoan(loan);

        var captor = ArgumentCaptor.forClass(List.class);
        verify(loanInstallmentRepository).saveAll(captor.capture());
        List<LoanInstallment> savedInstallments = captor.getValue();

        assertThat(savedInstallments).hasSize(12);
        assertThat(savedInstallments.getFirst().getDueDate().getDayOfMonth()).isEqualTo(1);
    }

    @Test
    void shouldReturnOnlyUnpaidInstallments() {
        Long loanId = 1L;
        List<LoanInstallment> unpaidInstallments = List.of(
                new LoanInstallment(1L, null, BigDecimal.valueOf(100), BigDecimal.ZERO, LocalDate.of(2024, 3, 1), null, false)
        );
        when(loanInstallmentRepository.findByLoanIdAndIsPaidFalseOrderByDueDateAsc(loanId)).thenReturn(unpaidInstallments);

        assertThat(loanInstallmentService.getUnpaidLoanInstallments(loanId)).hasSize(1);
    }

    @Test
    void shouldReturnEmptyListIfAllInstallmentsArePaid() {
        when(loanInstallmentRepository.findByLoanIdAndIsPaidFalseOrderByDueDateAsc(1L)).thenReturn(Collections.emptyList());
        assertThat(loanInstallmentService.getUnpaidLoanInstallments(1L)).isEmpty();
    }

    @Test
    void shouldSaveLoanInstallments() {
        List<LoanInstallment> installments = List.of(
                new LoanInstallment(1L, null, BigDecimal.valueOf(100), BigDecimal.ZERO, LocalDate.of(2024, 3, 1), null, false)
        );
        loanInstallmentService.saveAllLoanInstallments(installments);
        verify(loanInstallmentRepository).saveAll(installments);
    }

    @Test
    void shouldReturnTrueIfAllInstallmentsArePaid() {
        when(loanInstallmentRepository.countByLoanIdAndIsPaidFalse(1L)).thenReturn(0);
        assertThat(loanInstallmentService.checkIfInstallmentsArePaid(1L)).isTrue();
    }

    @Test
    void shouldReturnFalseIfThereAreUnpaidInstallments() {
        when(loanInstallmentRepository.countByLoanIdAndIsPaidFalse(1L)).thenReturn(2);
        assertThat(loanInstallmentService.checkIfInstallmentsArePaid(1L)).isFalse();
    }

}