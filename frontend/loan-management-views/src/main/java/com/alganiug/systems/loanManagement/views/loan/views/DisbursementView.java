package com.alganiug.systems.loanManagement.views.loan.views;

import com.alganiug.systems.loanManagement.core.services.loan.DisbursementService;
import com.alganiug.systems.loanManagement.models.loan.Disbursement;
import com.alganiug.systems.loanManagement.views.EntityView;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "disbursementView")
@ViewScoped
public class DisbursementView extends EntityView<Disbursement> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{disbursementServiceImpl}")
    private DisbursementService service;

    @Override
    protected DisbursementService getService() {
        return service;
    }

    public void setService(DisbursementService service) {
        this.service = service;
    }
}
