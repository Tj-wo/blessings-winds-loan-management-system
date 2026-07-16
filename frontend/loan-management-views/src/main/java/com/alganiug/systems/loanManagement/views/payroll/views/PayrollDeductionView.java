package com.alganiug.systems.loanManagement.views.payroll.views;

import com.alganiug.systems.loanManagement.core.services.payroll.PayrollDeductionService;
import com.alganiug.systems.loanManagement.models.payroll.PayrollDeduction;
import com.alganiug.systems.loanManagement.views.EntityView;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "payrollDeductionView")
@ViewScoped
public class PayrollDeductionView extends EntityView<PayrollDeduction> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{payrollDeductionServiceImpl}")
    private PayrollDeductionService service;

    @Override
    protected PayrollDeductionService getService() {
        return service;
    }

    public void setService(PayrollDeductionService service) {
        this.service = service;
    }
}
