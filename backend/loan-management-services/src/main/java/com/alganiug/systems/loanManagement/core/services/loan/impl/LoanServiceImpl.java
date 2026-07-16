package com.alganiug.systems.loanManagement.core.services.loan.impl;

import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.ServiceValidationException;
import com.alganiug.systems.loanManagement.core.services.loan.LoanService;
import com.alganiug.systems.loanManagement.models.loan.Loan;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class LoanServiceImpl extends GenericServiceImpl<Loan> implements LoanService {

    public LoanServiceImpl() {
        super(Loan.class);
    }

    @Override
    protected void validate(Loan loan) {
        requireText(loan.getLoanReference(), "Loan reference");
        requirePresent(loan.getEmployee(), "Employee");
        requirePresent(loan.getCompany(), "Company");
        requirePresent(loan.getProduct(), "Loan product");
        if (!sameEntity(loan.getCompany(), loan.getEmployee().getCompany())) {
            throw new ServiceValidationException("Loan company must match the employee company");
        }
        requirePositive(loan.getRequestedAmount(), "Requested amount");
        if (loan.getRequestedAmount().compareTo(loan.getProduct().getMinimumAmount()) < 0
                || loan.getRequestedAmount().compareTo(loan.getProduct().getMaximumAmount()) > 0) {
            throw new ServiceValidationException("Requested amount is outside the product limits");
        }
        if (loan.getRequestedTermMonths() < loan.getProduct().getMinimumTermMonths()
                || loan.getRequestedTermMonths() > loan.getProduct().getMaximumTermMonths()) {
            throw new ServiceValidationException("Requested term is outside the product limits");
        }
        if (loan.getApprovedAmount() != null && (loan.getApprovedAmount().compareTo(BigDecimal.ZERO) <= 0
                || loan.getApprovedAmount().compareTo(loan.getRequestedAmount()) > 0)) {
            throw new ServiceValidationException("Approved amount must be positive and cannot exceed requested amount");
        }
        requireText(loan.getPurpose(), "Loan purpose");
        requirePresent(loan.getPreferredDisbursementMethod(), "Preferred disbursement method");
        requireNonNegative(loan.getMonthlyInterestRate(), "Monthly interest rate");
        requireNonNegative(loan.getInterestAmount(), "Interest amount");
        requireNonNegative(loan.getInstallmentAmount(), "Installment amount");
        requireNonNegative(loan.getTotalPayable(), "Total payable");
        requireNonNegative(loan.getAmountPaid(), "Amount paid");
        requireNonNegative(loan.getOutstandingBalance(), "Outstanding balance");
        requirePresent(loan.getStatus(), "Loan status");
    }
}
