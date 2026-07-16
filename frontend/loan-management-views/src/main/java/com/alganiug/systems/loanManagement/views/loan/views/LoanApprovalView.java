package com.alganiug.systems.loanManagement.views.loan.views;

import com.alganiug.systems.loanManagement.core.services.loan.LoanApprovalService;
import com.alganiug.systems.loanManagement.models.loan.LoanApproval;
import com.alganiug.systems.loanManagement.views.EntityView;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "loanApprovalView")
@ViewScoped
public class LoanApprovalView extends EntityView<LoanApproval> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{loanApprovalServiceImpl}")
    private LoanApprovalService service;

    @Override
    protected LoanApprovalService getService() {
        return service;
    }

    public void setService(LoanApprovalService service) {
        this.service = service;
    }
}
