package com.alganiug.systems.loanManagement.views.company.views;

import com.alganiug.systems.loanManagement.core.services.company.CompanyService;
import com.alganiug.systems.loanManagement.models.company.Company;
import com.alganiug.systems.loanManagement.views.EntityView;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "companyView")
@ViewScoped
public class CompanyView extends EntityView<Company> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{companyServiceImpl}")
    private CompanyService service;

    @Override
    protected CompanyService getService() {
        return service;
    }

    public void setService(CompanyService service) {
        this.service = service;
    }
}
