package com.alganiug.systems.loanManagement.views.payroll.dialogs;

import com.alganiug.systems.loanManagement.core.services.payroll.PayrollDeductionBatchService;
import com.alganiug.systems.loanManagement.models.payroll.PayrollDeductionBatch;
import com.alganiug.systems.loanManagement.models.security.User;
import com.alganiug.systems.loanManagement.views.controllers.AuthenticationController;
import com.alganiug.systems.loanManagement.views.dialogs.DialogForm;
import com.alganiug.systems.loanManagement.views.navigation.LoanManagementHyperLinks;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;

@ManagedBean(name = "payrollDeductionBatchFormDialog")
@ViewScoped
public class PayrollDeductionBatchFormDialog extends DialogForm<PayrollDeductionBatch> {
    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{payrollDeductionBatchServiceImpl}")
    private PayrollDeductionBatchService service;
    @ManagedProperty(value = "#{authenticationController}")
    private AuthenticationController authenticationController;
    private String payrollPeriod;

    public PayrollDeductionBatchFormDialog() {
        super(LoanManagementHyperLinks.PAYROLL_BATCH_FORM_DIALOG, 720, 580);
        resetModal();
    }

    @Override protected PayrollDeductionBatchService getService() { return service; }

    @Override public void persist() {
        User user = authenticationController.getLoggedInUser();
        if (model.getCompany() == null && user != null) model.setCompany(user.getCompany());
        if (model.getUploadedBy() == null) model.setUploadedBy(user);
        if (model.getUploadedAt() == null) model.setUploadedAt(LocalDateTime.now());
        model.setPayrollPeriod(YearMonth.parse(payrollPeriod));
        if (model.getExpectedTotal() == null) model.setExpectedTotal(BigDecimal.ZERO);
        if (model.getReceivedTotal() == null) model.setReceivedTotal(BigDecimal.ZERO);
        super.persist();
    }

    @Override public void setFormProperties() {
        super.setFormProperties();
        payrollPeriod = model.getPayrollPeriod() == null ? null : model.getPayrollPeriod().toString();
    }

    @Override public void resetModal() {
        model = new PayrollDeductionBatch();
        model.setExpectedTotal(BigDecimal.ZERO);
        model.setReceivedTotal(BigDecimal.ZERO);
        payrollPeriod = YearMonth.now().toString();
        editing = false;
    }

    public String getPayrollPeriod() { return payrollPeriod; }
    public void setPayrollPeriod(String payrollPeriod) { this.payrollPeriod = payrollPeriod; }
    public void setService(PayrollDeductionBatchService service) { this.service = service; }
    public void setAuthenticationController(AuthenticationController authenticationController) { this.authenticationController = authenticationController; }
}