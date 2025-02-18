package controller;

import com.gkn.loanapp.model.dto.LoanPaymentRequestDto;
import com.gkn.loanapp.model.dto.LoanPaymentResponseDto;
import com.gkn.loanapp.security.annotations.HasAdminRole;
import com.gkn.loanapp.service.LoanPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/loan/pay")
@RequiredArgsConstructor
@HasAdminRole
public class LoanPaymentController {

    private final LoanPaymentService loanPaymentService;

    @PostMapping
    public ResponseEntity<LoanPaymentResponseDto> payLoan(@RequestBody LoanPaymentRequestDto requestDto) {
        return ResponseEntity.ok(loanPaymentService.payLoan(requestDto));
    }
}
