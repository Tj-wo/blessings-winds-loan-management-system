package com.alganiug.systems.loanManagement.views.loan.views;

import com.alganiug.systems.loanManagement.core.services.loan.LoanService;
import com.alganiug.systems.loanManagement.models.loan.Loan;
import com.alganiug.systems.loanManagement.models.security.User;
import com.alganiug.systems.loanManagement.views.controllers.AuthenticationController;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.UUID;

@ManagedBean(name = "loanAgreementView")
@ViewScoped
public class LoanAgreementView implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{loanServiceImpl}")
    private LoanService loanService;

    @ManagedProperty(value = "#{authenticationController}")
    private AuthenticationController authenticationController;

    private String loanId;
    private Loan loan;

    public void load() {
        if (loanId == null || loanId.trim().isEmpty()) {
            loan = null;
            return;
        }
        try {
            loan = loanService.getLoanDetails(UUID.fromString(loanId)).orElse(null);
            User user = authenticationController.getLoggedInUser();
            if (loan != null && user != null && user.getEmployee() != null
                    && !user.getEmployee().getId().equals(loan.getEmployee().getId())) {
                loan = null;
            }
        } catch (IllegalArgumentException ignored) {
            loan = null;
        }
    }

    public String getLoanId() { return loanId; }
    public void setLoanId(String loanId) { this.loanId = loanId; }
    public Loan getLoan() { return loan; }
    public void setLoanService(LoanService loanService) { this.loanService = loanService; }
    public void setAuthenticationController(AuthenticationController authenticationController) {
        this.authenticationController = authenticationController;
    }
}