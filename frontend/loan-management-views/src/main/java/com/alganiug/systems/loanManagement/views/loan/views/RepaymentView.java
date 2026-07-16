package com.alganiug.systems.loanManagement.views.loan.views;

import com.alganiug.systems.loanManagement.core.services.loan.RepaymentService;
import com.alganiug.systems.loanManagement.models.loan.Repayment;
import com.alganiug.systems.loanManagement.views.EntityView;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "repaymentView")
@ViewScoped
public class RepaymentView extends EntityView<Repayment> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{repaymentServiceImpl}")
    private RepaymentService service;

    @Override
    protected RepaymentService getService() {
        return service;
    }

    public void setService(RepaymentService service) {
        this.service = service;
    }
}
