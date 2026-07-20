package com.alganiug.systems.loanManagement.views.employee.controllers;

import com.alganiug.systems.loanManagement.core.services.company.CompanyService;
import com.alganiug.systems.loanManagement.core.services.employee.EmployeeService;
import com.alganiug.systems.loanManagement.models.company.Company;
import com.alganiug.systems.loanManagement.models.constants.EmploymentStatus;
import com.alganiug.systems.loanManagement.models.constants.KycStatus;
import com.alganiug.systems.loanManagement.models.employee.Employee;
import com.alganiug.systems.loanManagement.views.dialogs.MessageComposer;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

@ManagedBean(name = "employeeOnboardingController")
@ViewScoped
public class EmployeeOnboardingController implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(EmployeeOnboardingController.class.getName());

    @ManagedProperty(value = "#{companyServiceImpl}")
    private CompanyService companyService;

    @ManagedProperty(value = "#{employeeServiceImpl}")
    private EmployeeService employeeService;

    private String onboardingCode;
    private Company company;
    private Employee employee = new Employee();
    private boolean submitted;

    public void loadCompany() {
        company = companyService.findByOnboardingCode(onboardingCode).orElse(null);
    }

    public void register() {
        try {
            loadCompany();
            if (company == null) {
                throw new IllegalArgumentException("This employee registration link is invalid or has expired");
            }

            employee.setCompany(company);
            employee.setEmploymentStatus(EmploymentStatus.PERMANENT);
            employee.setKycStatus(KycStatus.PENDING_VERIFICATION);
            employeeService.saveInstance(employee);
            submitted = true;
            MessageComposer.info("Registration received",
                    "Your employee record was submitted to " + company.getName() + " for verification.");
        } catch (RuntimeException exception) {
            LOGGER.log(Level.SEVERE, "Employee self-registration failed", exception);
            MessageComposer.error("Registration failed", exception.getMessage());
        }
    }

    public String getOnboardingCode() { return onboardingCode; }
    public void setOnboardingCode(String onboardingCode) { this.onboardingCode = onboardingCode; }
    public Company getCompany() { return company; }
    public Employee getEmployee() { return employee; }
    public boolean isSubmitted() { return submitted; }
    public void setCompanyService(CompanyService companyService) { this.companyService = companyService; }
    public void setEmployeeService(EmployeeService employeeService) { this.employeeService = employeeService; }
}