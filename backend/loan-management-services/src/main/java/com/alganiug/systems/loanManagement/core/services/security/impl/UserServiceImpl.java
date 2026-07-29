package com.alganiug.systems.loanManagement.core.services.security.impl;

import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.ServiceValidationException;
import com.alganiug.systems.loanManagement.core.services.security.UserService;
import com.alganiug.systems.loanManagement.core.services.notification.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.security.SecureRandom;

@Service
public class UserServiceImpl extends GenericServiceImpl<User> implements UserService {

    private static final String PASSWORD_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789!@#$%";
    private final SecureRandom secureRandom = new SecureRandom();

    @Autowired
    private EmailService emailService;


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

        if (!actorHasPermission("EMPLOYEE_ACCOUNT_ACTIVATE")) {
            throw new ServiceValidationException("You are not allowed to activate employee accounts");
        }
        Employee managedEmployee = entityManager.find(Employee.class, employee.getId());
        if (managedEmployee == null || managedEmployee.getRecordStatus() == RecordStatus.DELETED) {
            throw new ServiceValidationException("Employee does not exist");
        }

        UUID actorCompanyId = actorCompanyId();
        if (actorCompanyId != null && !actorCompanyId.equals(managedEmployee.getCompany().getId())) {
            throw new ServiceValidationException("You cannot activate an employee outside your company");
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
        String temporaryPassword = temporaryPassword();
        user.setPasswordHash(PasswordUtil.hash(temporaryPassword));
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
    public void requestPasswordResetOtp(String email) {
        requireText(email, "Email address");
        String lookup = email.trim().toLowerCase();
        Optional<User> match = entityManager.createQuery("select user from User user where lower(user.email) = :lookup", User.class)
                .setParameter("lookup", lookup).getResultStream().findFirst();
        if (!match.isPresent()) return;
        User user = match.get();
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        if (user.getPasswordResetOtpHash() != null && user.getPasswordResetOtpExpiresAt() != null
                && user.getPasswordResetOtpExpiresAt().isAfter(now.plusMinutes(9))) {
            throw new ServiceValidationException("Please wait one minute before requesting another verification code");
        }
        String otp = String.format("%06d", secureRandom.nextInt(1_000_000));
        user.setPasswordResetOtpHash(PasswordUtil.hash("OTP:" + otp));
        user.setPasswordResetOtpExpiresAt(java.time.LocalDateTime.now().plusMinutes(10));
        user.setPasswordResetOtpAttempts(0);
        entityManager.merge(user);
        emailService.send(user.getEmail(), "Your Blessed Winds Loans verification code",
                "Your password reset verification code is: " + otp
                        + "\n\nThis code expires in 10 minutes. Do not share it with anyone.");
    }

    @Override
    public void resetPasswordWithOtp(String email, String otp, String newPassword) {
        requireText(email, "Email address");
        requireText(otp, "Verification code");
        requireText(newPassword, "New password");
        if (newPassword.length() < 8) throw new ServiceValidationException("Password must contain at least 8 characters");
        User user = entityManager.createQuery("select user from User user where lower(user.email) = :lookup", User.class)
                .setParameter("lookup", email.trim().toLowerCase()).getResultStream().findFirst()
                .orElseThrow(() -> new ServiceValidationException("The verification code is invalid or expired"));
        if (user.getPasswordResetOtpHash() == null || user.getPasswordResetOtpExpiresAt() == null
                || user.getPasswordResetOtpExpiresAt().isBefore(java.time.LocalDateTime.now())
                || user.getPasswordResetOtpAttempts() >= 5) {
            clearPasswordResetOtp(user);
            throw new ServiceValidationException("The verification code is invalid or expired. Request a new code.");
        }
        if (!PasswordUtil.matches("OTP:" + otp.trim(), user.getPasswordResetOtpHash())) {
            user.setPasswordResetOtpAttempts(user.getPasswordResetOtpAttempts() + 1);
            entityManager.merge(user);
            throw new ServiceValidationException("The verification code is invalid or expired");
        }
        user.setPasswordHash(PasswordUtil.hash(newPassword));
        clearPasswordResetOtp(user);
        entityManager.merge(user);
    }

    private void clearPasswordResetOtp(User user) {
        user.setPasswordResetOtpHash(null);
        user.setPasswordResetOtpExpiresAt(null);
        user.setPasswordResetOtpAttempts(0);
    }

    private String temporaryPassword() {
        StringBuilder password = new StringBuilder(14);
        for (int i = 0; i < 14; i++) password.append(PASSWORD_CHARS.charAt(secureRandom.nextInt(PASSWORD_CHARS.length())));
        return password.toString();
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
