package com.alganiug.systems.loanManagement.core.services.security;

import com.alganiug.systems.loanManagement.core.services.GenericService;
import com.alganiug.systems.loanManagement.models.security.User;
import com.alganiug.systems.loanManagement.models.employee.Employee;
import com.alganiug.systems.loanManagement.models.constants.AccountStatus;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserService extends GenericService<User> {

    String DEFAULT_EMPLOYEE_PASSWORD = "Welcome123!";

    String DEFAULT_COMPANY_CONTACT_PASSWORD = "Welcome123!";

    boolean hasPermission(UUID userId, String permissionCode);

    Optional<User> authenticate(String username, String password);

    Set<String> getPermissionCodes(UUID userId);

    boolean hasRole(UUID userId, String roleName);

    User activateEmployeeAccount(Employee employee);

    Set<UUID> getEmployeeIdsWithAccounts();

    boolean accountExists(String usernameOrEmail);

    void requestPasswordResetOtp(String email);

    void resetPasswordWithOtp(String email, String otp, String newPassword);

    User updateAccountStatus(User user, AccountStatus accountStatus);
}
