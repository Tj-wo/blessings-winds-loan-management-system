package com.alganiug.systems.loanManagement.views.employee.dialogs;

import com.alganiug.systems.loanManagement.views.navigation.LoanManagementHyperLinks;
import com.alganiug.systems.loanManagement.core.services.employee.EmployeeService;
import com.alganiug.systems.loanManagement.models.employee.Employee;
import com.alganiug.systems.loanManagement.views.dialogs.DialogForm;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "employeeFormDialog")
@ViewScoped
public class EmployeeFormDialog extends DialogForm<Employee> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{employeeServiceImpl}")
    private EmployeeService service;

    public EmployeeFormDialog() {
        super(LoanManagementHyperLinks.EMPLOYEE_FORM_DIALOG, 700, 550);
        resetModal();
    }

    @Override
    protected EmployeeService getService() {
        return service;
    }

    public void setService(EmployeeService service) {
        this.service = service;
    }

    @Override
    public void resetModal() {
        model = new Employee();
        editing = false;
    }
}
