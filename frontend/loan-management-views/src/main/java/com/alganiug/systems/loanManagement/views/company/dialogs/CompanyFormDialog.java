package com.alganiug.systems.loanManagement.views.company.dialogs;

import com.alganiug.systems.loanManagement.core.services.company.CompanyService;
import com.alganiug.systems.loanManagement.models.company.Company;
import com.alganiug.systems.loanManagement.views.dialogs.DialogForm;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "companyFormDialog")
@ViewScoped
public class CompanyFormDialog extends DialogForm<Company> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{companyServiceImpl}")
    private CompanyService service;

    public CompanyFormDialog() {
        super("/pages/company/CompanyFormDialog", 700, 550);
    }

    @Override
    protected CompanyService getService() {
        return service;
    }

    public void setService(CompanyService service) {
        this.service = service;
    }

    @Override
    public void resetModal() {
        model = new Company();
        editing = false;
    }
}
