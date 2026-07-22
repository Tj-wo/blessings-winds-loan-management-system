package com.alganiug.systems.loanManagement.views.payroll.dialogs;

import com.alganiug.systems.loanManagement.core.services.loan.LoanService;
import com.alganiug.systems.loanManagement.core.services.payroll.PayrollDeductionBatchService;
import com.alganiug.systems.loanManagement.core.services.payroll.PayrollDeductionService;
import com.alganiug.systems.loanManagement.models.constants.LoanStatus;
import com.alganiug.systems.loanManagement.models.loan.Loan;
import com.alganiug.systems.loanManagement.models.payroll.PayrollDeduction;
import com.alganiug.systems.loanManagement.models.payroll.PayrollDeductionBatch;
import com.alganiug.systems.loanManagement.models.security.User;
import com.alganiug.systems.loanManagement.views.controllers.AuthenticationController;
import com.alganiug.systems.loanManagement.views.dialogs.DialogForm;
import com.alganiug.systems.loanManagement.views.navigation.LoanManagementHyperLinks;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ManagedBean(name = "payrollDeductionFormDialog")
@ViewScoped
public class PayrollDeductionFormDialog extends DialogForm<PayrollDeduction> {
    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{payrollDeductionServiceImpl}")
    private PayrollDeductionService service;
    @ManagedProperty(value = "#{payrollDeductionBatchServiceImpl}")
    private PayrollDeductionBatchService batchService;
    @ManagedProperty(value = "#{loanServiceImpl}")
    private LoanService loanService;
    @ManagedProperty(value = "#{authenticationController}")
    private AuthenticationController authenticationController;

    public PayrollDeductionFormDialog() {
        super(LoanManagementHyperLinks.PAYROLL_DEDUCTION_FORM_DIALOG, 760, 600);
        resetModal();
    }

    @Override protected PayrollDeductionService getService() { return service; }

    public List<PayrollDeductionBatch> getCompanyBatches() {
        User user = currentUser();
        return user == null || user.getCompany() == null ? Collections.emptyList()
                : batchService.getForCompany(user.getCompany().getId());
    }

    public List<Loan> getCompanyLoans() {
        User user = currentUser();
        if (user == null || user.getCompany() == null) return Collections.emptyList();
        return loanService.getLoansForCompany(user.getCompany().getId()).stream()
                .filter(loan -> loan.getStatus() == LoanStatus.DISBURSED || loan.getStatus() == LoanStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    @Override public void persist() {
        if (model.getLoan() != null) {
            model.setEmployee(model.getLoan().getEmployee());
            if (model.getExpectedAmount() == null) model.setExpectedAmount(model.getLoan().getInstallmentAmount());
        }
        if (model.getDeductedAmount() == null) model.setDeductedAmount(BigDecimal.ZERO);
        super.persist();
    }

    private User currentUser() {
        return authenticationController == null ? null : authenticationController.getLoggedInUser();
    }

    @Override public void resetModal() { model = new PayrollDeduction(); editing = false; }
    public void setService(PayrollDeductionService service) { this.service = service; }
    public void setBatchService(PayrollDeductionBatchService batchService) { this.batchService = batchService; }
    public void setLoanService(LoanService loanService) { this.loanService = loanService; }
    public void setAuthenticationController(AuthenticationController authenticationController) { this.authenticationController = authenticationController; }
}