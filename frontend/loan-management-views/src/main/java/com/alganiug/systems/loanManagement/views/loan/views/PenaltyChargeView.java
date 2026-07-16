package com.alganiug.systems.loanManagement.views.loan.views;

import com.alganiug.systems.loanManagement.core.services.loan.PenaltyChargeService;
import com.alganiug.systems.loanManagement.models.loan.PenaltyCharge;
import com.alganiug.systems.loanManagement.views.EntityView;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "penaltyChargeView")
@ViewScoped
public class PenaltyChargeView extends EntityView<PenaltyCharge> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{penaltyChargeServiceImpl}")
    private PenaltyChargeService service;

    @Override
    protected PenaltyChargeService getService() {
        return service;
    }

    public void setService(PenaltyChargeService service) {
        this.service = service;
    }
}
