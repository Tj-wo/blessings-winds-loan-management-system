package com.alganiug.systems.loanManagement.views.loan.dialogs;

import com.alganiug.systems.loanManagement.core.services.loan.LoanProductService;
import com.alganiug.systems.loanManagement.models.loan.LoanProduct;
import com.alganiug.systems.loanManagement.views.dialogs.DialogForm;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "loanProductFormDialog")
@ViewScoped
public class LoanProductFormDialog extends DialogForm<LoanProduct> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{loanProductServiceImpl}")
    private LoanProductService service;

    public LoanProductFormDialog() {
        super("/pages/loan/LoanProductFormDialog", 700, 550);
    }

    @Override
    protected LoanProductService getService() {
        return service;
    }

    public void setService(LoanProductService service) {
        this.service = service;
    }

    @Override
    public void resetModal() {
        model = new LoanProduct();
        editing = false;
    }
}
