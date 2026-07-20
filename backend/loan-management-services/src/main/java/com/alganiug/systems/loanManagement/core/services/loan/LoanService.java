package com.alganiug.systems.loanManagement.core.services.loan;

import com.alganiug.systems.loanManagement.core.services.GenericService;
import com.alganiug.systems.loanManagement.models.loan.Loan;

import java.util.List;
import java.util.UUID;

public interface LoanService extends GenericService<Loan> {

    Loan disburse(Loan loan);

    List<Loan> getLoansForEmployee(UUID employeeId);

    List<Loan> getLoansForCompany(UUID companyId);

    List<Loan> getAllLoansWithDetails();

    long countActiveLoans(UUID companyId);

    java.util.Optional<Loan> getLoanDetails(UUID loanId);
}
