package com.alganiug.systems.loanManagement.views.employee.dialogs;

import com.alganiug.systems.loanManagement.core.services.company.CompanyService;
import com.alganiug.systems.loanManagement.core.services.employee.EmployeeService;
import com.alganiug.systems.loanManagement.models.company.Company;
import com.alganiug.systems.loanManagement.models.employee.Employee;
import com.alganiug.systems.loanManagement.models.constants.EmploymentStatus;
import com.alganiug.systems.loanManagement.views.dialogs.DialogForm;
import com.alganiug.systems.loanManagement.views.navigation.LoanManagementHyperLinks;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.Collections;
import java.util.List;

@ManagedBean(name = "employeeFormDialog")
@ViewScoped
public class EmployeeFormDialog extends DialogForm<Employee> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{employeeServiceImpl}")
    private EmployeeService service;

    @ManagedProperty(value = "#{companyServiceImpl}")
    private CompanyService companyService;

    public EmployeeFormDialog() {
        super(LoanManagementHyperLinks.EMPLOYEE_FORM_DIALOG, 820, 650);
        resetModal();
    }

    @Override
    protected EmployeeService getService() {
        return service;
    }

    @Override
    public void resetModal() {
        model = new Employee();
        editing = false;
    }

    public EmploymentStatus[] getEmploymentStatuses() {
        return EmploymentStatus.values();
    }

    public List<Company> getCompanies() {
        return companyService == null ? Collections.emptyList() : companyService.getAllInstances();
    }

    public void setService(EmployeeService service) {
        this.service = service;
    }

    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }
}