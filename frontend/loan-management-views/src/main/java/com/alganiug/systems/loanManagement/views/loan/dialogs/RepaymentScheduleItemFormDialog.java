package com.alganiug.systems.loanManagement.views.loan.dialogs;

import com.alganiug.systems.loanManagement.views.navigation.LoanManagementHyperLinks;
import com.alganiug.systems.loanManagement.core.services.loan.RepaymentScheduleItemService;
import com.alganiug.systems.loanManagement.models.loan.RepaymentScheduleItem;
import com.alganiug.systems.loanManagement.views.dialogs.DialogForm;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "repaymentScheduleItemFormDialog")
@ViewScoped
public class RepaymentScheduleItemFormDialog extends DialogForm<RepaymentScheduleItem> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{repaymentScheduleItemServiceImpl}")
    private RepaymentScheduleItemService service;

    public RepaymentScheduleItemFormDialog() {
        super(LoanManagementHyperLinks.REPAYMENT_SCHEDULE_FORM_DIALOG, 700, 550);
        resetModal();
    }

    @Override
    protected RepaymentScheduleItemService getService() {
        return service;
    }

    public void setService(RepaymentScheduleItemService service) {
        this.service = service;
    }

    @Override
    public void resetModal() {
        model = new RepaymentScheduleItem();
        editing = false;
    }
}
