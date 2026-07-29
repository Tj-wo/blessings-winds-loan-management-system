package com.alganiug.systems.loanManagement.views.employee.views;

import com.alganiug.systems.loanManagement.core.services.employee.EmployeeService;
import com.alganiug.systems.loanManagement.core.services.security.UserService;
import com.alganiug.systems.loanManagement.models.employee.Employee;
import com.alganiug.systems.loanManagement.models.security.User;
import com.alganiug.systems.loanManagement.views.EntityView;
import com.alganiug.systems.loanManagement.views.dialogs.MessageComposer;
import com.alganiug.systems.loanManagement.views.controllers.AuthenticationController;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@ManagedBean(name = "employeeView")
@ViewScoped
public class EmployeeView extends EntityView<Employee> {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(EmployeeView.class.getName());

    @ManagedProperty(value = "#{employeeServiceImpl}")
    private EmployeeService service;

    @ManagedProperty(value = "#{userServiceImpl}")
    private UserService userService;

    @ManagedProperty(value = "#{authenticationController}")
    private AuthenticationController authenticationController;

    private Set<UUID> employeeIdsWithAccounts = new LinkedHashSet<>();

    @Override
    protected EmployeeService getService() {
        return service;
    }

    @Override
    public void reload() {
        User loggedInUser = authenticationController == null ? null : authenticationController.getLoggedInUser();
        if (loggedInUser != null && loggedInUser.getEmployee() != null) {
            setRecords(java.util.Collections.singletonList(loggedInUser.getEmployee()));
        } else if (loggedInUser != null && loggedInUser.getCompany() != null) {
            setRecords(service.getEmployeesForCompany(loggedInUser.getCompany().getId()));
        } else {
            super.reload();
        }
        if (userService != null) {
            employeeIdsWithAccounts = userService.getEmployeeIdsWithAccounts();
        }
    }

    public long getKycPendingCount() {
        return getRecords().stream().filter(employee -> employee.getKycStatus()
                == com.alganiug.systems.loanManagement.models.constants.KycStatus.PENDING_VERIFICATION
                || employee.getKycStatus() == com.alganiug.systems.loanManagement.models.constants.KycStatus.REQUIRES_MORE_INFORMATION).count();
    }

    public long getKycApprovedCount() {
        return getRecords().stream().filter(employee -> employee.getKycStatus()
                == com.alganiug.systems.loanManagement.models.constants.KycStatus.APPROVED).count();
    }
    public void activateAccount(Employee employee) {
        try {
            User user = userService.activateEmployeeAccount(employee);
            employeeIdsWithAccounts.add(employee.getId());
            MessageComposer.info("Account activated",
                    "Login details were emailed to " + user.getEmail() + ".");
        } catch (RuntimeException exception) {
            LOGGER.log(Level.SEVERE, "Failed to activate employee account", exception);
            MessageComposer.error("Account activation failed", exception.getMessage());
        }
    }

    public boolean hasUserAccount(Employee employee) {
        return employee != null && employee.getId() != null && employeeIdsWithAccounts.contains(employee.getId());
    }

    public void setAuthenticationController(AuthenticationController authenticationController) {
        this.authenticationController = authenticationController;
    }

    public void setService(EmployeeService service) {
        this.service = service;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}