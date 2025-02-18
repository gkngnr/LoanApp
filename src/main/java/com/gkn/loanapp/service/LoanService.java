package com.example.demo.service;

import com.example.demo.model.dto.LoanRequestDto;
import com.example.demo.model.dto.LoanResponseDto;
import com.example.demo.model.entity.Customer;
import com.example.demo.model.entity.Loan;
import com.example.demo.model.exception.LoanNotFoundException;
import com.example.demo.model.mapper.LoanMapper;
import com.example.demo.repository.LoanRepository;
import com.example.demo.security.enums.Role;
import com.example.demo.service.validator.LoanCreateValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.example.demo.security.SecurityUtils.hasAdminRole;
import static com.example.demo.security.SecurityUtils.hasCustomerRole;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoanService {

    private final CustomerService customerService;
    private final LoanRepository loanRepository;
    private final LoanInstallmentService loanInstallmentService;
    private final LoanCreateValidator validator;

    public List<LoanResponseDto> getAll(UserDetails userDetails) {
        if (hasAdminRole(userDetails)) {
            return loanRepository.findAll().stream().map(LoanMapper::toLoanResponseDto).toList();
        } else if (hasCustomerRole(userDetails)) {
            return loanRepository.findByCustomerUsername(userDetails.getUsername())
                    .stream().map(LoanMapper::toLoanResponseDto).toList();
        }
        throw new LoanNotFoundException("Loan not found with user name: " + userDetails.getUsername());
    }

    public LoanResponseDto getById(Long id, UserDetails userDetails) {
        if (hasAdminRole(userDetails)) {
            return loanRepository.findById(id)
                    .map(LoanMapper::toLoanResponseDto)
                    .orElseThrow(() -> new LoanNotFoundException("Loan not found with id: " + id));
        } else if (hasCustomerRole(userDetails)) {
            return loanRepository.findByIdAndCustomerUsername(id, userDetails.getUsername())
                    .map(LoanMapper::toLoanResponseDto)
                    .orElseThrow(() -> new LoanNotFoundException("Loan not found with id: " + id));
        }
        throw new LoanNotFoundException("Loan not found with user name: " + userDetails.getUsername());
    }

    @Transactional
    public LoanResponseDto create(LoanRequestDto loanRequestDto) {
        final var customer = customerService.getCustomerEntityById(loanRequestDto.customerId());
        validator.validateLoanRequest(customer, loanRequestDto);
        final var loan = mapToLoan(loanRequestDto, customer);
        final var loanResponseDto = LoanMapper.toLoanResponseDto(loanRepository.save(loan));
        loanInstallmentService.generateInstallmentsFromLoan(loan);
        customerService.updateCustomerUsedCreditLimit(customer, loan.getLoanAmount());
        return loanResponseDto;
    }

    @Transactional
    public void updateLoanAsPaid(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found with id: " + loanId));
        loan.setPaid(true);
    }

    private Loan mapToLoan(LoanRequestDto loanRequestDto, Customer customer) {
        return Loan.builder()
                .loanAmount(calculateTotalLoanAmount(loanRequestDto))
                .interestRate(loanRequestDto.interestRate())
                .numberOfInstallments(loanRequestDto.numberOfInstallments())
                .customer(customer)
                .createDate(LocalDate.now())
                .build();
    }

    private BigDecimal calculateTotalLoanAmount(LoanRequestDto loanRequestDto) {
        return loanRequestDto.loanAmount().multiply(BigDecimal.valueOf(1 + loanRequestDto.interestRate().doubleValue()));
    }

}
