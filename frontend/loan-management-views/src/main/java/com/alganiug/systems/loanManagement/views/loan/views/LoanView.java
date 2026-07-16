package com.alganiug.systems.loanManagement.views.loan.views;

import com.alganiug.systems.loanManagement.core.services.loan.LoanService;
import com.alganiug.systems.loanManagement.models.loan.Loan;
import com.alganiug.systems.loanManagement.views.EntityView;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "loanView")
@ViewScoped
public class LoanView extends EntityView<Loan> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{loanServiceImpl}")
    private LoanService service;

    @Override
    protected LoanService getService() {
        return service;
    }

    public void setService(LoanService service) {
        this.service = service;
    }
}
