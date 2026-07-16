package com.alganiug.systems.loanManagement.views.loan.dialogs;

import com.alganiug.systems.loanManagement.core.services.loan.LoanApprovalService;
import com.alganiug.systems.loanManagement.models.loan.LoanApproval;
import com.alganiug.systems.loanManagement.views.dialogs.DialogForm;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "loanApprovalFormDialog")
@ViewScoped
public class LoanApprovalFormDialog extends DialogForm<LoanApproval> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{loanApprovalServiceImpl}")
    private LoanApprovalService service;

    public LoanApprovalFormDialog() {
        super("/pages/loan/LoanApprovalFormDialog", 700, 550);
    }

    @Override
    protected LoanApprovalService getService() {
        return service;
    }

    public void setService(LoanApprovalService service) {
        this.service = service;
    }

    @Override
    public void resetModal() {
        model = new LoanApproval();
        editing = false;
    }
}
