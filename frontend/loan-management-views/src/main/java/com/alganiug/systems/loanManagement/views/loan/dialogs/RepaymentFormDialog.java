package com.alganiug.systems.loanManagement.views.loan.dialogs;

import com.alganiug.systems.loanManagement.views.navigation.LoanManagementHyperLinks;
import com.alganiug.systems.loanManagement.core.services.loan.RepaymentService;
import com.alganiug.systems.loanManagement.models.loan.Repayment;
import com.alganiug.systems.loanManagement.views.dialogs.DialogForm;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "repaymentFormDialog")
@ViewScoped
public class RepaymentFormDialog extends DialogForm<Repayment> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{repaymentServiceImpl}")
    private RepaymentService service;

    public RepaymentFormDialog() {
        super(LoanManagementHyperLinks.REPAYMENT_FORM_DIALOG, 700, 550);
        resetModal();
    }

    @Override
    protected RepaymentService getService() {
        return service;
    }

    public void setService(RepaymentService service) {
        this.service = service;
    }

    @Override
    public void resetModal() {
        model = new Repayment();
        editing = false;
    }
}
