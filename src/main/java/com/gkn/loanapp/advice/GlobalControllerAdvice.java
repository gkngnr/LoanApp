package com.example.demo.advice;

import com.example.demo.model.exception.CustomerNotFoundException;
import com.example.demo.model.exception.LoanInstallmentNotFoundException;
import com.example.demo.model.exception.LoanNotFoundException;
import com.example.demo.model.exception.LoanValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(LoanValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDto> handleValidationException(LoanValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDto(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponseDto> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        var errorResponse = new ErrorResponseDto(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(LoanNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponseDto> handleLoanNotFoundException(LoanNotFoundException ex) {
        var errorResponse = new ErrorResponseDto(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(LoanInstallmentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponseDto> handleLoanNotFoundException(LoanInstallmentNotFoundException ex) {
        var errorResponse = new ErrorResponseDto(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


    public record ErrorResponseDto(HttpStatus status, String message) {}
}
