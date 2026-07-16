package com.alganiug.systems.loanManagement.core.services.employee.impl;

import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.ServiceValidationException;
import com.alganiug.systems.loanManagement.core.services.employee.EmployeeService;
import com.alganiug.systems.loanManagement.models.employee.Employee;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends GenericServiceImpl<Employee> implements EmployeeService {

    public EmployeeServiceImpl() {
        super(Employee.class);
    }

    @Override
    protected void validate(Employee employee) {
        requirePresent(employee.getCompany(), "Company");
        requireText(employee.getEmployeeNumber(), "Employee number");
        requireText(employee.getPayrollNumber(), "Payroll number");
        requireText(employee.getFirstName(), "First name");
        requireText(employee.getLastName(), "Last name");
        requireText(employee.getNationalId(), "National ID");
        requireText(employee.getPhone(), "Phone number");
        requirePresent(employee.getEmploymentStatus(), "Employment status");
        requireNonNegative(employee.getMonthlySalary(), "Monthly salary");
        requirePresent(employee.getKycStatus(), "KYC status");
        if (employee.getRiskScore() < 0 || employee.getRiskScore() > 100) {
            throw new ServiceValidationException("Risk score must be between 0 and 100");
        }
    }
}
