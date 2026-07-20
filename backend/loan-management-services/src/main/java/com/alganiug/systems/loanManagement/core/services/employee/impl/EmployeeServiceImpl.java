package com.alganiug.systems.loanManagement.core.services.employee.impl;

import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.ServiceValidationException;
import com.alganiug.systems.loanManagement.core.services.employee.EmployeeService;
import com.alganiug.systems.loanManagement.models.employee.Employee;
import com.alganiug.systems.loanManagement.models.constants.RecordStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Collections;
import java.util.UUID;

@Service
public class EmployeeServiceImpl extends GenericServiceImpl<Employee> implements EmployeeService {

    public EmployeeServiceImpl() {
        super(Employee.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getInstances(int offset, int limit) {
        if (offset < 0) {
            throw new ServiceValidationException("Offset cannot be negative");
        }
        if (limit < 1) {
            throw new ServiceValidationException("Limit must be greater than zero");
        }

        return entityManager.createQuery(
                        "select employee from Employee employee join fetch employee.company "
                                + "where employee.recordStatus = :recordStatus "
                                + "order by employee.createdAt desc",
                        Employee.class)
                .setParameter("recordStatus", RecordStatus.ACTIVE)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getAllInstances() {
        return entityManager.createQuery(
                        "select employee from Employee employee join fetch employee.company "
                                + "where employee.recordStatus = :recordStatus "
                                + "order by employee.createdAt desc",
                        Employee.class)
                .setParameter("recordStatus", RecordStatus.ACTIVE)
                .getResultList();
    }
    @Override
    @Transactional(readOnly = true)
    public List<Employee> getEmployeesForCompany(UUID companyId) {
        if (companyId == null) return Collections.emptyList();
        return entityManager.createQuery("select employee from Employee employee join fetch employee.company "
                        + "where employee.company.id = :companyId and employee.recordStatus = :status "
                        + "order by employee.createdAt desc", Employee.class)
                .setParameter("companyId", companyId)
                .setParameter("status", RecordStatus.ACTIVE)
                .getResultList();
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
