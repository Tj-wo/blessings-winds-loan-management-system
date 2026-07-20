package com.alganiug.systems.loanManagement.core.services.employee;

import com.alganiug.systems.loanManagement.core.services.GenericService;
import com.alganiug.systems.loanManagement.models.employee.Employee;
import java.util.List;
import java.util.UUID;

public interface EmployeeService extends GenericService<Employee> {
    List<Employee> getEmployeesForCompany(UUID companyId);
}
