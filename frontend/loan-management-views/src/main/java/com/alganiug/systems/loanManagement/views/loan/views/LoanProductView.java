package com.alganiug.systems.loanManagement.views.loan.views;

import com.alganiug.systems.loanManagement.core.services.loan.LoanProductService;
import com.alganiug.systems.loanManagement.models.loan.LoanProduct;
import com.alganiug.systems.loanManagement.views.EntityView;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "loanProductView")
@ViewScoped
public class LoanProductView extends EntityView<LoanProduct> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{loanProductServiceImpl}")
    private LoanProductService service;

    @Override
    protected LoanProductService getService() {
        return service;
    }

    public void setService(LoanProductService service) {
        this.service = service;
    }
}
