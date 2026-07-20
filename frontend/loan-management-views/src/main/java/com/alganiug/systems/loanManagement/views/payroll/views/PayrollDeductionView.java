package com.alganiug.systems.loanManagement.views.payroll.views;

import com.alganiug.systems.loanManagement.core.services.payroll.PayrollDeductionService;
import com.alganiug.systems.loanManagement.models.payroll.PayrollDeduction;
import com.alganiug.systems.loanManagement.views.EntityView;
import com.alganiug.systems.loanManagement.views.controllers.AuthenticationController;
import com.alganiug.systems.loanManagement.models.security.User;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "payrollDeductionView")
@ViewScoped
public class PayrollDeductionView extends EntityView<PayrollDeduction> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{payrollDeductionServiceImpl}")
    private PayrollDeductionService service;

    @ManagedProperty(value = "#{authenticationController}")
    private AuthenticationController authenticationController;

    @Override
    protected PayrollDeductionService getService() {
        return service;
    }

    @Override
    public void reload() {
        User user = authenticationController == null ? null : authenticationController.getLoggedInUser();
        if (user != null && user.getCompany() != null) {
            setRecords(service.getForCompany(user.getCompany().getId()));
            return;
        }
        super.reload();
    }

    public void setAuthenticationController(AuthenticationController authenticationController) {
        this.authenticationController = authenticationController;
    }

    public void setService(PayrollDeductionService service) {
        this.service = service;
    }
}
