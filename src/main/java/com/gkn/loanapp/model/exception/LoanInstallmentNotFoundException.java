package com.example.demo.model.exception;

public class LoanInstallmentNotFoundException extends RuntimeException {
    public LoanInstallmentNotFoundException(String message) {
        super(message);
    }
}
