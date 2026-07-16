package com.alganiug.systems.loanManagement.views.loan.dialogs;

import com.alganiug.systems.loanManagement.core.services.loan.PenaltyChargeService;
import com.alganiug.systems.loanManagement.models.loan.PenaltyCharge;
import com.alganiug.systems.loanManagement.views.dialogs.DialogForm;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "penaltyChargeFormDialog")
@ViewScoped
public class PenaltyChargeFormDialog extends DialogForm<PenaltyCharge> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{penaltyChargeServiceImpl}")
    private PenaltyChargeService service;

    public PenaltyChargeFormDialog() {
        super("/pages/loan/PenaltyChargeFormDialog", 700, 550);
    }

    @Override
    protected PenaltyChargeService getService() {
        return service;
    }

    public void setService(PenaltyChargeService service) {
        this.service = service;
    }

    @Override
    public void resetModal() {
        model = new PenaltyCharge();
        editing = false;
    }
}
