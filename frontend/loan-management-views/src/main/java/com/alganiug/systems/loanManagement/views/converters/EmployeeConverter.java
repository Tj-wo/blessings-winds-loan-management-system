package com.alganiug.systems.loanManagement.views.converters;

import com.alganiug.systems.loanManagement.core.services.employee.EmployeeService;
import com.alganiug.systems.loanManagement.models.employee.Employee;

import javax.faces.convert.FacesConverter;

@FacesConverter("employeeConverter")
public class EmployeeConverter extends EntityConverter<Employee> {

    public EmployeeConverter() {
        super("#{employeeServiceImpl}", EmployeeService.class);
    }
}
