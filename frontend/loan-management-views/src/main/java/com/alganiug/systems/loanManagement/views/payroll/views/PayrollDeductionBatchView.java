package com.alganiug.systems.loanManagement.views.payroll.views;

import com.alganiug.systems.loanManagement.core.services.payroll.PayrollDeductionBatchService;
import com.alganiug.systems.loanManagement.models.payroll.PayrollDeductionBatch;
import com.alganiug.systems.loanManagement.views.EntityView;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "payrollDeductionBatchView")
@ViewScoped
public class PayrollDeductionBatchView extends EntityView<PayrollDeductionBatch> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{payrollDeductionBatchServiceImpl}")
    private PayrollDeductionBatchService service;

    @Override
    protected PayrollDeductionBatchService getService() {
        return service;
    }

    public void setService(PayrollDeductionBatchService service) {
        this.service = service;
    }
}
