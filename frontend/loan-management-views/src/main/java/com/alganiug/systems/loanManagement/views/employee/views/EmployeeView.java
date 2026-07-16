package com.alganiug.systems.loanManagement.views.employee.views;

import com.alganiug.systems.loanManagement.core.services.employee.EmployeeService;
import com.alganiug.systems.loanManagement.models.employee.Employee;
import com.alganiug.systems.loanManagement.views.EntityView;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "employeeView")
@ViewScoped
public class EmployeeView extends EntityView<Employee> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{employeeServiceImpl}")
    private EmployeeService service;

    @Override
    protected EmployeeService getService() {
        return service;
    }

    public void setService(EmployeeService service) {
        this.service = service;
    }
}
