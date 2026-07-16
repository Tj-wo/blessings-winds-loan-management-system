package com.alganiug.systems.loanManagement.core.services.security.impl;

import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.ServiceValidationException;
import com.alganiug.systems.loanManagement.core.services.security.UserService;
import com.alganiug.systems.loanManagement.models.security.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
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
}
