package com.example.demo.repository;

import com.example.demo.model.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    @Query("SELECT l FROM Loan l JOIN FETCH l.customer WHERE l.customer.username = :username") //overcoming n+1
    List<Loan> findByCustomerUsername(String username);

    @Query("SELECT l FROM Loan l JOIN FETCH l.customer WHERE l.id = :loanId AND l.customer.username = :username") //overcoming n+1
    Optional<Loan> findByIdAndCustomerUsername(Long loanId, String username);
}
