package com.alganiug.systems.loanManagement.core.services.security.impl;

import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.ServiceValidationException;
import com.alganiug.systems.loanManagement.core.services.security.UserService;
import com.alganiug.systems.loanManagement.models.security.User;
import com.alganiug.systems.loanManagement.models.security.Role;
import com.alganiug.systems.loanManagement.models.security.RoleConstants;
import com.alganiug.systems.loanManagement.models.employee.Employee;
import com.alganiug.systems.loanManagement.models.constants.AccountStatus;
import com.alganiug.systems.loanManagement.models.constants.RecordStatus;
import com.alganiug.systems.loanManagement.utils.PasswordUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl extends GenericServiceImpl<User> implements UserService {


    public UserServiceImpl() {
        super(User.class);
    }

    @Override
    protected void validate(User user) {
        requireText(user.getUsername(), "Username");
        requireText(user.getPasswordHash(), "Password hash");
        requireText(user.getDisplayName(), "Display name");
        requirePresent(user.getAccountStatus(), "Account status");
        if (user.getEmployee() != null && user.getCompany() == null) {
            throw new ServiceValidationException("An employee user must have a company");
        }
        if (user.getEmployee() != null && !sameEntity(user.getCompany(), user.getEmployee().getCompany())) {
            throw new ServiceValidationException("User company must match the employee company");
        }
        if (user.getRoles().isEmpty()) {
            throw new ServiceValidationException("At least one role is required");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasPermission(UUID userId, String permissionCode) {
        if (userId == null || permissionCode == null || permissionCode.trim().isEmpty()) {
            return false;
        }

        Long matches = entityManager.createQuery("select count(assignment) from User user join user.roles role "
                + "join role.rolePermissions assignment join assignment.permission permission "
                + "where user.id = :userId and permission.code = :permissionCode "
                + "and user.recordStatus = com.alganiug.systems.loanManagement.models.constants.RecordStatus.ACTIVE "
                + "and role.recordStatus = com.alganiug.systems.loanManagement.models.constants.RecordStatus.ACTIVE "
                + "and assignment.recordStatus = com.alganiug.systems.loanManagement.models.constants.RecordStatus.ACTIVE "
                + "and permission.recordStatus = com.alganiug.systems.loanManagement.models.constants.RecordStatus.ACTIVE",
                Long.class).setParameter("userId", userId).setParameter("permissionCode", permissionCode.trim())
                .getSingleResult();
        return matches > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> authenticate(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null) {
            return Optional.empty();
        }
        Optional<User> user = entityManager
                .createQuery("select distinct user from User user left join fetch user.roles left join fetch user.company scopedCompany left join fetch user.employee employee left join fetch employee.company "
                        + "where lower(user.username) = :username and user.recordStatus = :recordStatus "
                        + "and user.accountStatus = :accountStatus", User.class)
                .setParameter("username", username.trim().toLowerCase())
                .setParameter("recordStatus", RecordStatus.ACTIVE).setParameter("accountStatus", AccountStatus.ACTIVE)
                .getResultStream().findFirst();
        return user.filter(candidate -> PasswordUtil.matches(password, candidate.getPasswordHash()));
    }

    @Override
    @Transactional(readOnly = true)
    public Set<String> getPermissionCodes(UUID userId) {
        if (userId == null) {
            return new LinkedHashSet<>();
        }

        return new LinkedHashSet<>(entityManager.createQuery(
                "select distinct permission.code from User user join user.roles role "
                        + "join role.rolePermissions assignment join assignment.permission permission "
                        + "where user.id = :userId "
                        + "and user.recordStatus = com.alganiug.systems.loanManagement.models.constants.RecordStatus.ACTIVE "
                        + "and role.recordStatus = com.alganiug.systems.loanManagement.models.constants.RecordStatus.ACTIVE "
                        + "and assignment.recordStatus = com.alganiug.systems.loanManagement.models.constants.RecordStatus.ACTIVE "
                        + "and permission.recordStatus = com.alganiug.systems.loanManagement.models.constants.RecordStatus.ACTIVE",
                String.class).setParameter("userId", userId).getResultList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasRole(UUID userId, String roleName) {
        if (userId == null || roleName == null || roleName.trim().isEmpty()) {
            return false;
        }

        Long matches = entityManager.createQuery("select count(role) from User user join user.roles role "
                + "where user.id = :userId and role.name = :roleName "
                + "and user.recordStatus = com.alganiug.systems.loanManagement.models.constants.RecordStatus.ACTIVE "
                + "and role.recordStatus = com.alganiug.systems.loanManagement.models.constants.RecordStatus.ACTIVE",
                Long.class).setParameter("userId", userId).setParameter("roleName", roleName.trim()).getSingleResult();
        return matches > 0;
    }

    @Override
    public User activateEmployeeAccount(Employee employee) {
        if (employee == null || employee.getId() == null) {
            throw new ServiceValidationException("A saved employee is required");
        }

        Employee managedEmployee = entityManager.find(Employee.class, employee.getId());
        if (managedEmployee == null || managedEmployee.getRecordStatus() == RecordStatus.DELETED) {
            throw new ServiceValidationException("Employee does not exist");
        }

        requireText(managedEmployee.getEmail(), "Employee email");
        String username = managedEmployee.getEmail().trim().toLowerCase();

        boolean accountExists = !entityManager.createQuery(
                        "select user.id from User user where user.employee.id = :employeeId or lower(user.username) = :username",
                        UUID.class)
                .setParameter("employeeId", managedEmployee.getId())
                .setParameter("username", username)
                .setMaxResults(1)
                .getResultList()
                .isEmpty();
        if (accountExists) {
            throw new ServiceValidationException("An account already exists for this employee or email address");
        }

        Role employeeRole = entityManager.createQuery(
                        "select role from Role role where role.name = :name and role.recordStatus = :recordStatus",
                        Role.class)
                .setParameter("name", RoleConstants.ROLE_EMPLOYEE)
                .setParameter("recordStatus", RecordStatus.ACTIVE)
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> new ServiceValidationException("Employee role is not configured"));

        User user = new User();
        user.setUsername(username);
        user.setEmail(username);
        user.setDisplayName(managedEmployee.getFirstName() + " " + managedEmployee.getLastName());
        user.setPasswordHash(PasswordUtil.hash(UserService.DEFAULT_EMPLOYEE_PASSWORD));
        user.setAccountStatus(AccountStatus.ACTIVE);
        user.setCompany(managedEmployee.getCompany());
        user.setEmployee(managedEmployee);
        user.getRoles().add(employeeRole);
        entityManager.persist(user);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<UUID> getEmployeeIdsWithAccounts() {
        return new LinkedHashSet<>(entityManager.createQuery(
                        "select user.employee.id from User user where user.employee is not null "
                                + "and user.recordStatus = :recordStatus",
                        UUID.class)
                .setParameter("recordStatus", RecordStatus.ACTIVE)
                .getResultList());
    }
    @Override
    @Transactional(readOnly = true)
    public boolean accountExists(String usernameOrEmail) {
        if (usernameOrEmail == null || usernameOrEmail.trim().isEmpty()) {
            return false;
        }
        String lookup = usernameOrEmail.trim().toLowerCase();
        return entityManager.createQuery(
                        "select count(user) from User user where lower(user.username) = :lookup "
                                + "or lower(user.email) = :lookup", Long.class)
                .setParameter("lookup", lookup)
                .getSingleResult() > 0;
    }

    @Override
    public void resetPassword(String usernameOrEmail, String newPassword) {
        requireText(usernameOrEmail, "Username or email");
        requireText(newPassword, "New password");
        if (newPassword.length() < 8) {
            throw new ServiceValidationException("Password must contain at least 8 characters");
        }
        String lookup = usernameOrEmail.trim().toLowerCase();
        User user = entityManager.createQuery(
                        "select user from User user where lower(user.username) = :lookup or lower(user.email) = :lookup",
                        User.class)
                .setParameter("lookup", lookup)
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> new ServiceValidationException("No account was found for that username or email"));
        user.setPasswordHash(PasswordUtil.hash(newPassword));
        entityManager.merge(user);
    }

    @Override
    public User updateAccountStatus(User user, AccountStatus accountStatus) {
        requirePresent(user, "User");
        requirePresent(accountStatus, "Account status");
        User managedUser = getInstanceById(user.getId())
                .orElseThrow(() -> new ServiceValidationException("User account was not found"));
        managedUser.setAccountStatus(accountStatus);
        return entityManager.merge(managedUser);
    }}
