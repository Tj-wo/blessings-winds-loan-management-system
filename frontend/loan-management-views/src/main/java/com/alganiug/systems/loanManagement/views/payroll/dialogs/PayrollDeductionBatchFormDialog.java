package com.alganiug.systems.loanManagement.views.payroll.dialogs;

import com.alganiug.systems.loanManagement.views.navigation.LoanManagementHyperLinks;
import com.alganiug.systems.loanManagement.core.services.payroll.PayrollDeductionBatchService;
import com.alganiug.systems.loanManagement.models.payroll.PayrollDeductionBatch;
import com.alganiug.systems.loanManagement.views.dialogs.DialogForm;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "payrollDeductionBatchFormDialog")
@ViewScoped
public class PayrollDeductionBatchFormDialog extends DialogForm<PayrollDeductionBatch> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{payrollDeductionBatchServiceImpl}")
    private PayrollDeductionBatchService service;

    public PayrollDeductionBatchFormDialog() {
        super(LoanManagementHyperLinks.PAYROLL_BATCH_FORM_DIALOG, 700, 550);
        resetModal();
    }

    @Override
    protected PayrollDeductionBatchService getService() {
        return service;
    }

    public void setService(PayrollDeductionBatchService service) {
        this.service = service;
    }

    @Override
    public void resetModal() {
        model = new PayrollDeductionBatch();
        editing = false;
    }
}
