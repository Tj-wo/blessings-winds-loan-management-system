package com.alganiug.systems.loanManagement.core.services.impl;

import com.alganiug.systems.loanManagement.core.services.MigrationService;
import com.alganiug.systems.loanManagement.models.constants.AccountStatus;
import com.alganiug.systems.loanManagement.models.constants.RecordStatus;
import com.alganiug.systems.loanManagement.models.constants.SettingValueType;
import com.alganiug.systems.loanManagement.models.security.Permission;
import com.alganiug.systems.loanManagement.models.security.PermissionConstants;
import com.alganiug.systems.loanManagement.models.security.Role;
import com.alganiug.systems.loanManagement.models.security.RoleConstants;
import com.alganiug.systems.loanManagement.models.security.RolePermission;
import com.alganiug.systems.loanManagement.models.security.SystemPermission;
import com.alganiug.systems.loanManagement.models.security.SystemRole;
import com.alganiug.systems.loanManagement.models.security.User;
import com.alganiug.systems.loanManagement.models.settings.SystemSetting;
import com.alganiug.systems.loanManagement.utils.PasswordUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Service
public class MigrationServiceImpl implements MigrationService {

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${loan.bootstrap.admin-username:admin}")
    private String administratorUsername;

    @Value("${loan.bootstrap.admin-password:ChangeMe123!}")
    private String administratorPassword;

    @Value("${loan.bootstrap.admin-email:admin@alganiug.com}")
    private String administratorEmail;

    private boolean completed;

    @Override
    @EventListener(ContextRefreshedEvent.class)
    @Transactional
    public synchronized void migrate() {
        if (completed) {
            return;
        }

        Map<String, Permission> permissions = seedPermissions();
        Map<String, Role> roles = seedRoles();
        seedRolePermissions(roles, permissions);
        seedSettings();
        seedAdministrator(roles.get(RoleConstants.ROLE_ADMINISTRATOR));
        entityManager.flush();
        completed = true;
    }

    private Map<String, Permission> seedPermissions() {
        Map<String, Permission> permissions = new LinkedHashMap<>();
        for (Field field : PermissionConstants.class.getDeclaredFields()) {
            SystemPermission definition = field.getAnnotation(SystemPermission.class);
            if (definition == null || !Modifier.isStatic(field.getModifiers()) || field.getType() != String.class) {
                continue;
            }

            String code = readPermissionCode(field);
            Permission permission = findPermission(code)
                    .orElseGet(() -> createPermission(code, definition.name(), definition.description()));
            permission.setName(definition.name());
            permission.setDescription(definition.description());
            permission.setRecordStatus(RecordStatus.ACTIVE);
            permissions.put(code, permission);
        }
        return permissions;
    }

    private String readPermissionCode(Field field) {
        try {
            return (String) field.get(null);
        } catch (IllegalAccessException exception) {
            throw new IllegalStateException("Unable to read permission constant " + field.getName(), exception);
        }
    }

    private Map<String, Role> seedRoles() {
        Map<String, Role> roles = new LinkedHashMap<>();
        for (Field field : RoleConstants.class.getDeclaredFields()) {
            SystemRole definition = field.getAnnotation(SystemRole.class);
            if (definition == null || !Modifier.isStatic(field.getModifiers()) || field.getType() != String.class) {
                continue;
            }

            String roleName = readRoleName(field);
            Role role = findRole(roleName).orElseGet(() -> createRole(roleName, definition.description()));
            role.setDescription(definition.description());
            role.setRecordStatus(RecordStatus.ACTIVE);
            roles.put(roleName, role);
        }
        return roles;
    }

    private String readRoleName(Field field) {
        try {
            return (String) field.get(null);
        } catch (IllegalAccessException exception) {
            throw new IllegalStateException("Unable to read role constant " + field.getName(), exception);
        }
    }

    private void seedRolePermissions(Map<String, Role> roles, Map<String, Permission> permissions) {
        permissions.forEach((code, permission) -> attach(roles.get(RoleConstants.ROLE_ADMINISTRATOR), permission));

        permissions.forEach((code, permission) -> {
            if (isLoanManagerPermission(code)) {
                attach(roles.get(RoleConstants.ROLE_LOAN_MANAGER), permission);
            }
            if (isHrPermission(code)) {
                attach(roles.get(RoleConstants.ROLE_HR_SUPERVISOR), permission);
            }
            if (isEmployeePermission(code)) {
                attach(roles.get(RoleConstants.ROLE_EMPLOYEE), permission);
            }
        });
    }

    private void seedSettings() {
        seedSetting("system.currency", "UGX", SettingValueType.STRING, "GENERAL", "System currency");
        seedSetting("system.organisation.name", "Blessed Winds Loans", SettingValueType.STRING, "GENERAL",
                "Client business name");
        seedSetting("loan.documents.max-upload-bytes", "10485760", SettingValueType.INTEGER, "DOCUMENTS",
                "Maximum document upload size");
        seedSetting("security.bootstrap-admin-password-change-required", "true", SettingValueType.BOOLEAN, "SECURITY",
                "Requires the bootstrap administrator password to be changed");
    }

    private void seedAdministrator(Role administratorRole) {
        Optional<User> existing = entityManager
                .createQuery("select user from User user where lower(user.username) = :username", User.class)
                .setParameter("username", administratorUsername.trim().toLowerCase(Locale.ROOT)).getResultStream()
                .findFirst();
        if (existing.isPresent()) {
            return;
        }

        User administrator = new User();
        administrator.setUsername(administratorUsername.trim());
        administrator.setPasswordHash(PasswordUtil.hash(administratorPassword));
        administrator.setDisplayName("System Administrator");
        administrator.setEmail(administratorEmail);
        administrator.setAccountStatus(AccountStatus.ACTIVE);
        administrator.getRoles().add(administratorRole);
        entityManager.persist(administrator);
    }

    private Optional<Permission> findPermission(String code) {
        return entityManager.createQuery("select permission from Permission permission where permission.code = :code",
                Permission.class).setParameter("code", code).getResultStream().findFirst();
    }

    private Permission createPermission(String code, String name, String description) {
        Permission permission = new Permission();
        permission.setCode(code);
        permission.setName(name);
        permission.setDescription(description);
        entityManager.persist(permission);
        return permission;
    }

    private Optional<Role> findRole(String name) {
        return entityManager.createQuery("select role from Role role where role.name = :name", Role.class)
                .setParameter("name", name).getResultStream().findFirst();
    }

    private Role createRole(String name, String description) {
        Role role = new Role();
        role.setName(name);
        role.setDescription(description);
        entityManager.persist(role);
        return role;
    }

    private void attach(Role role, Permission permission) {
        Optional<RolePermission> existing = entityManager
                .createQuery("select assignment from RolePermission assignment where assignment.role = :role "
                        + "and assignment.permission = :permission", RolePermission.class)
                .setParameter("role", role).setParameter("permission", permission).getResultStream().findFirst();
        if (existing.isPresent()) {
            existing.get().setRecordStatus(RecordStatus.ACTIVE);
            return;
        }

        RolePermission assignment = new RolePermission();
        assignment.setRole(role);
        assignment.setPermission(permission);
        entityManager.persist(assignment);
    }

    private void seedSetting(String key, String value, SettingValueType type, String category, String description) {
        boolean exists = !entityManager
                .createQuery("select setting from SystemSetting setting where setting.settingKey = :key",
                        SystemSetting.class)
                .setParameter("key", key).setMaxResults(1).getResultList().isEmpty();
        if (!exists) {
            SystemSetting setting = new SystemSetting();
            setting.setSettingKey(key);
            setting.setSettingValue(value);
            setting.setValueType(type);
            setting.setCategory(category);
            setting.setDescription(description);
            entityManager.persist(setting);
        }
    }

    private boolean isAdministratorPermission(String code) {
        return startsWithAny(code, "USER_", "ROLE_", "PERMISSION_", "COMPANY_", "EMPLOYEE_", "DOCUMENT_",
                "LOAN_PRODUCT_", "SYSTEM_SETTING_", "AUDIT_LOG_", "NOTIFICATION_")
                || code.endsWith("_VIEW") || code.equals("REPORT_EXPORT");
    }

    private boolean isLoanManagerPermission(String code) {
        return startsWithAny(code, "LOAN_PRODUCT_")
                || code.equals("LOAN_VIEW") || code.equals("LOAN_EDIT")
                || code.equals("LOAN_APPROVAL_VIEW") || code.equals("LOAN_APPROVAL_CREATE")
                || code.equals("LOAN_APPROVE") || code.equals("LOAN_REJECT") || code.equals("LOAN_DISBURSE")
                || code.equals("DISBURSEMENT_VIEW") || code.equals("REPAYMENT_VIEW")
                || code.equals("REPAYMENT_SCHEDULE_VIEW") || code.equals("PENALTY_VIEW")
                || code.equals("PENALTY_WAIVE") || code.equals("COMPANY_VIEW")
                || code.equals("EMPLOYEE_VIEW") || code.equals("DOCUMENT_VIEW")
                || code.equals("REPORT_EXPORT") || code.equals("NOTIFICATION_VIEW");
    }

    private boolean isHrPermission(String code) {
        return code.equals("COMPANY_VIEW") || startsWithAny(code, "EMPLOYEE_", "DOCUMENT_")
                || code.equals("PAYROLL_BATCH_CREATE") || code.equals("PAYROLL_BATCH_VIEW")
                || code.equals("PAYROLL_BATCH_EDIT") || code.equals("PAYROLL_BATCH_POST")
                || code.equals("PAYROLL_DEDUCTION_VIEW") || code.equals("LOAN_VIEW")
                || code.equals("LOAN_APPROVAL_CREATE") || code.equals("LOAN_APPROVAL_VIEW")
                || code.equals("LOAN_APPROVE") || code.equals("LOAN_REJECT")
                || code.equals("REPORT_EXPORT") || code.equals("NOTIFICATION_VIEW");
    }

    private boolean isEmployeePermission(String code) {
        return code.equals("LOAN_CREATE") || code.equals("LOAN_VIEW") || code.equals("LOAN_EDIT")
                || code.equals("DOCUMENT_CREATE")
                || code.equals("DOCUMENT_VIEW") || code.equals("NOTIFICATION_VIEW") || code.equals("EMPLOYEE_VIEW")
                || code.equals("EMPLOYEE_EDIT");
    }
    private boolean startsWithAny(String value, String... prefixes) {
        return Arrays.stream(prefixes).anyMatch(value::startsWith);
    }

}
