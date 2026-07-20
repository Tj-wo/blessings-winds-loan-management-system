package com.alganiug.systems.loanManagement.views.company.views;

import com.alganiug.systems.loanManagement.core.services.company.CompanyService;
import com.alganiug.systems.loanManagement.models.company.Company;
import com.alganiug.systems.loanManagement.views.EntityView;
import com.alganiug.systems.loanManagement.views.controllers.AuthenticationController;
import com.alganiug.systems.loanManagement.models.security.User;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "companyView")
@ViewScoped
public class CompanyView extends EntityView<Company> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{companyServiceImpl}")
    private CompanyService service;

    @ManagedProperty(value = "#{authenticationController}")
    private AuthenticationController authenticationController;

    @Override
    protected CompanyService getService() {
        return service;
    }

    @Override
    public void reload() {
        User user = authenticationController == null ? null : authenticationController.getLoggedInUser();
        if (user != null && user.getCompany() != null && user.getEmployee() == null) {
            setRecords(java.util.Collections.singletonList(user.getCompany()));
            return;
        }
        super.reload();
    }

    public void setAuthenticationController(AuthenticationController authenticationController) {
        this.authenticationController = authenticationController;
    }
    public void setService(CompanyService service) {
        this.service = service;
    }
}
