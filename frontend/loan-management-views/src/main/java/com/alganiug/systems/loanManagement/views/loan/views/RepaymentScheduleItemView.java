package com.alganiug.systems.loanManagement.views.loan.views;

import com.alganiug.systems.loanManagement.core.services.loan.RepaymentScheduleItemService;
import com.alganiug.systems.loanManagement.models.loan.RepaymentScheduleItem;
import com.alganiug.systems.loanManagement.views.EntityView;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "repaymentScheduleItemView")
@ViewScoped
public class RepaymentScheduleItemView extends EntityView<RepaymentScheduleItem> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{repaymentScheduleItemServiceImpl}")
    private RepaymentScheduleItemService service;

    @Override
    protected RepaymentScheduleItemService getService() {
        return service;
    }

    public void setService(RepaymentScheduleItemService service) {
        this.service = service;
    }
}
