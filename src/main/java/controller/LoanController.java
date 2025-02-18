package controller;

import com.gkn.loanapp.model.dto.LoanInstallmentResponseDto;
import com.gkn.loanapp.model.dto.LoanRequestDto;
import com.gkn.loanapp.model.dto.LoanResponseDto;
import com.gkn.loanapp.security.annotations.HasAdminRole;
import com.gkn.loanapp.service.LoanInstallmentService;
import com.gkn.loanapp.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor
@com.gkn.loanapp.security.annotations.HasAdminOrCustomerRole
public class LoanController {

    private final LoanService loanService;
    private final LoanInstallmentService loanInstallmentService;

    @GetMapping
    public ResponseEntity<List<LoanResponseDto>> getAll(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(loanService.getAll(userDetails));
    }

    @GetMapping("/{loanId}")
    public ResponseEntity<LoanResponseDto> getById(@PathVariable Long loanId,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(loanService.getById(loanId, userDetails));
    }

    @PostMapping
    @HasAdminRole
    public ResponseEntity<LoanResponseDto> create(@RequestBody @Valid LoanRequestDto loanRequestDto,
                                                  UriComponentsBuilder uriComponentsBuilder) {
        final var createdLoan = loanService.create(loanRequestDto);
        final var httpHeaders = getHttpHeaders(uriComponentsBuilder, createdLoan);
        return new ResponseEntity<>(createdLoan, httpHeaders, HttpStatus.CREATED);
    }

    @GetMapping("/{loanId}/installments")
    public ResponseEntity<List<LoanInstallmentResponseDto>> getLoanInstallmentsByLoanId(@PathVariable Long loanId) {
        return ResponseEntity.ok(loanInstallmentService.getLoanInstallmentsByLoanId(loanId));
    }

    private HttpHeaders getHttpHeaders(UriComponentsBuilder uriComponentsBuilder, LoanResponseDto createdLoan) {
        final var uriComponents = uriComponentsBuilder
                .path("/api/v1/loans/{id}")
                .buildAndExpand(createdLoan.id());
        final var httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponents.toUri());
        return httpHeaders;
    }

}
