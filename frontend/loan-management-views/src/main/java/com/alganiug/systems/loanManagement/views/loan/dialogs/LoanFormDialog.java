package com.alganiug.systems.loanManagement.views.loan.dialogs;

import com.alganiug.systems.loanManagement.core.services.loan.LoanService;
import com.alganiug.systems.loanManagement.models.loan.Loan;
import com.alganiug.systems.loanManagement.views.dialogs.DialogForm;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "loanFormDialog")
@ViewScoped
public class LoanFormDialog extends DialogForm<Loan> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{loanServiceImpl}")
    private LoanService service;

    public LoanFormDialog() {
        super("/pages/loan/LoanFormDialog", 700, 550);
    }

    @Override
    protected LoanService getService() {
        return service;
    }

    public void setService(LoanService service) {
        this.service = service;
    }

    @Override
    public void resetModal() {
        model = new Loan();
        editing = false;
    }
}
