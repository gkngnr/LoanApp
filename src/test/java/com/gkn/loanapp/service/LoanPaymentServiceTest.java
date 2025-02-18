package com.example.demo.service;

import com.example.demo.model.dto.LoanPaymentRequestDto;
import com.example.demo.model.dto.LoanPaymentResponseDto;
import com.example.demo.model.entity.Loan;
import com.example.demo.model.entity.LoanInstallment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanPaymentServiceTest {

    @InjectMocks
    private LoanPaymentService loanPaymentService;

    @Mock
    private LoanService loanService;

    @Mock
    private LoanInstallmentService loanInstallmentService;

    private static final Long LOAN_ID = 1L;
    private final Loan loan = Loan.builder().id(LOAN_ID).build();

    @Test
    void shouldPayLoanSuccessfully_WhenAmountIsSufficient() {
        List<LoanInstallment> unpaidInstallments = createMockUnpaidInstallments();
        LoanPaymentRequestDto requestDto = new LoanPaymentRequestDto(LOAN_ID, BigDecimal.valueOf(200));

        when(loanInstallmentService.getUnpaidLoanInstallments(LOAN_ID)).thenReturn(unpaidInstallments);
        when(loanInstallmentService.checkIfInstallmentsArePaid(LOAN_ID)).thenReturn(true);

        LoanPaymentResponseDto response = loanPaymentService.payLoan(requestDto);

        assertEquals(2, response.installmentsPaid());
        assertTrue(response.isLoanFullyPaid());

        verify(loanInstallmentService).saveAllLoanInstallments(unpaidInstallments);
        verify(loanService).updateLoanAsPaid(LOAN_ID);
    }

    @Test
    void shouldNotPayInstallment_WhenAmountIsNotEnough() {
        // Given
        List<LoanInstallment> unpaidInstallments = createMockUnpaidInstallments();
        LoanPaymentRequestDto requestDto = new LoanPaymentRequestDto(LOAN_ID, BigDecimal.valueOf(50));

        when(loanInstallmentService.getUnpaidLoanInstallments(LOAN_ID)).thenReturn(unpaidInstallments);

        LoanPaymentResponseDto response = loanPaymentService.payLoan(requestDto);

        assertEquals(0, response.installmentsPaid());
        assertEquals(BigDecimal.ZERO, response.totalAmountSpent());
        assertFalse(response.isLoanFullyPaid());

        verify(loanService, never()).updateLoanAsPaid(anyLong());
    }

    @Test
    void shouldNotPayInstallmentsBeyondAllowedPeriod() {
        List<LoanInstallment> installments = createMockUnpaidInstallmentsBeyond3Months();
        LoanPaymentRequestDto requestDto = new LoanPaymentRequestDto(LOAN_ID, BigDecimal.valueOf(300));

        when(loanInstallmentService.getUnpaidLoanInstallments(LOAN_ID)).thenReturn(installments);

        LoanPaymentResponseDto response = loanPaymentService.payLoan(requestDto);

        assertEquals(0, response.installmentsPaid());
        assertEquals(BigDecimal.ZERO, response.totalAmountSpent());
        assertFalse(response.isLoanFullyPaid());

        verify(loanService, never()).updateLoanAsPaid(anyLong());
    }

    @Test
    void shouldApplyPenaltyForLatePayments() {
        List<LoanInstallment> installments = createMockLateInstallments();
        LoanPaymentRequestDto requestDto = new LoanPaymentRequestDto(LOAN_ID, BigDecimal.valueOf(110));

        when(loanInstallmentService.getUnpaidLoanInstallments(LOAN_ID)).thenReturn(installments);

        LoanPaymentResponseDto response = loanPaymentService.payLoan(requestDto);

        assertEquals(1, response.installmentsPaid());
        assertTrue(response.totalAmountSpent().compareTo(BigDecimal.valueOf(100)) > 0);
        assertFalse(response.isLoanFullyPaid());

        verify(loanInstallmentService).saveAllLoanInstallments(installments);
    }

    @Test
    void shouldApplyRewardForEarlyPayments() {
        List<LoanInstallment> installments = createMockEarlyInstallments();
        LoanPaymentRequestDto requestDto = new LoanPaymentRequestDto(LOAN_ID, BigDecimal.valueOf(99));

        when(loanInstallmentService.getUnpaidLoanInstallments(LOAN_ID)).thenReturn(installments);

        LoanPaymentResponseDto response = loanPaymentService.payLoan(requestDto);

        assertEquals(1, response.installmentsPaid());
        assertTrue(response.totalAmountSpent().compareTo(BigDecimal.valueOf(100)) < 0);
        assertFalse(response.isLoanFullyPaid());

        verify(loanInstallmentService).saveAllLoanInstallments(installments);
    }

    // Helper methods for test data

    private List<LoanInstallment> createMockUnpaidInstallments() {
        return List.of(
                new LoanInstallment(1L, loan, BigDecimal.valueOf(100), BigDecimal.ZERO, LocalDate.now().plusMonths(1).withDayOfMonth(1), null, false),
                new LoanInstallment(2L, loan, BigDecimal.valueOf(100), BigDecimal.ZERO, LocalDate.now().plusMonths(2).withDayOfMonth(1), null, false)
        );
    }

    private List<LoanInstallment> createMockUnpaidInstallmentsBeyond3Months() {
        return List.of(
                new LoanInstallment(3L, loan, BigDecimal.valueOf(100), BigDecimal.ZERO, LocalDate.now().plusMonths(4), null, false)
        );
    }

    private List<LoanInstallment> createMockLateInstallments() {
        return List.of(
                new LoanInstallment(4L, loan, BigDecimal.valueOf(100), BigDecimal.ZERO, LocalDate.now().minusDays(10), null, false)
        );
    }

    private List<LoanInstallment> createMockEarlyInstallments() {
        return List.of(
                new LoanInstallment(5L, loan, BigDecimal.valueOf(100), BigDecimal.ZERO, LocalDate.now().plusDays(20), null, false)
        );
    }

}
