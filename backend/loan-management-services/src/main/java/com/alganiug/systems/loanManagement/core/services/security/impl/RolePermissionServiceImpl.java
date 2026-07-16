package com.alganiug.systems.loanManagement.core.services.security.impl;

import com.alganiug.systems.loanManagement.core.services.ServiceValidationException;
import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.security.RolePermissionService;
import com.alganiug.systems.loanManagement.models.constants.RecordStatus;
import com.alganiug.systems.loanManagement.models.security.Permission;
import com.alganiug.systems.loanManagement.models.security.Role;
import com.alganiug.systems.loanManagement.models.security.RolePermission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RolePermissionServiceImpl extends GenericServiceImpl<RolePermission> implements RolePermissionService {

    public RolePermissionServiceImpl() {
        super(RolePermission.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RolePermission> getByRole(Role role) {
        if (role == null || role.getId() == null) {
            return Collections.emptyList();
        }

        return entityManager
                .createQuery(
                        "select assignment from RolePermission assignment join fetch assignment.permission permission "
                                + "where assignment.role.id = :roleId and assignment.recordStatus = :recordStatus "
                                + "and permission.recordStatus = :recordStatus order by permission.name",
                        RolePermission.class)
                .setParameter("roleId", role.getId()).setParameter("recordStatus", RecordStatus.ACTIVE).getResultList();
    }

    @Override
    @Transactional
    public void synchronize(Role role, Collection<Permission> permissions) {
        if (role == null || role.getId() == null) {
            throw new ServiceValidationException("A saved role is required before assigning permissions");
        }

        Set<UUID> selectedIds = permissions == null ? Collections.emptySet()
                : permissions.stream().filter(permission -> permission != null && permission.getId() != null)
                        .map(Permission::getId).collect(Collectors.toCollection(LinkedHashSet::new));

        List<RolePermission> assignments = entityManager
                .createQuery("select assignment from RolePermission assignment join fetch assignment.permission "
                        + "where assignment.role.id = :roleId", RolePermission.class)
                .setParameter("roleId", role.getId()).getResultList();

        Set<UUID> assignedPermissionIds = new LinkedHashSet<>();
        for (RolePermission assignment : assignments) {
            UUID permissionId = assignment.getPermission().getId();
            assignedPermissionIds.add(permissionId);
            assignment.setRecordStatus(selectedIds.contains(permissionId) ? RecordStatus.ACTIVE : RecordStatus.DELETED);
            entityManager.merge(assignment);
        }

        if (permissions != null) {
            for (Permission permission : permissions) {
                if (permission == null || permission.getId() == null
                        || assignedPermissionIds.contains(permission.getId())) {
                    continue;
                }
                RolePermission assignment = new RolePermission();
                assignment.setRole(role);
                assignment.setPermission(permission);
                entityManager.persist(assignment);
            }
        }
    }

    @Override
    protected void validate(RolePermission rolePermission) {
        requirePresent(rolePermission.getRole(), "Role");
        requirePresent(rolePermission.getPermission(), "Permission");
    }
}
