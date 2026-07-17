package com.alganiug.systems.loanManagement.views.payroll.dialogs;

import com.alganiug.systems.loanManagement.views.navigation.LoanManagementHyperLinks;
import com.alganiug.systems.loanManagement.core.services.payroll.PayrollDeductionService;
import com.alganiug.systems.loanManagement.models.payroll.PayrollDeduction;
import com.alganiug.systems.loanManagement.views.dialogs.DialogForm;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "payrollDeductionFormDialog")
@ViewScoped
public class PayrollDeductionFormDialog extends DialogForm<PayrollDeduction> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{payrollDeductionServiceImpl}")
    private PayrollDeductionService service;

    public PayrollDeductionFormDialog() {
        super(LoanManagementHyperLinks.PAYROLL_DEDUCTION_FORM_DIALOG, 700, 550);
        resetModal();
    }

    @Override
    protected PayrollDeductionService getService() {
        return service;
    }

    public void setService(PayrollDeductionService service) {
        this.service = service;
    }

    @Override
    public void resetModal() {
        model = new PayrollDeduction();
        editing = false;
    }
}
