package com.alganiug.systems.loanManagement.views.loan.dialogs;

import com.alganiug.systems.loanManagement.views.navigation.LoanManagementHyperLinks;
import com.alganiug.systems.loanManagement.core.services.loan.DisbursementService;
import com.alganiug.systems.loanManagement.models.loan.Disbursement;
import com.alganiug.systems.loanManagement.views.dialogs.DialogForm;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "disbursementFormDialog")
@ViewScoped
public class DisbursementFormDialog extends DialogForm<Disbursement> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{disbursementServiceImpl}")
    private DisbursementService service;

    public DisbursementFormDialog() {
        super(LoanManagementHyperLinks.DISBURSEMENT_FORM_DIALOG, 700, 550);
        resetModal();
    }

    @Override
    protected DisbursementService getService() {
        return service;
    }

    public void setService(DisbursementService service) {
        this.service = service;
    }

    @Override
    public void resetModal() {
        model = new Disbursement();
        editing = false;
    }
}
