package com.alganiug.systems.loanManagement.core.services.security;

import com.alganiug.systems.loanManagement.core.services.GenericService;
import com.alganiug.systems.loanManagement.models.security.Permission;
import com.alganiug.systems.loanManagement.models.security.Role;
import com.alganiug.systems.loanManagement.models.security.RolePermission;

import java.util.Collection;
import java.util.List;

public interface RolePermissionService extends GenericService<RolePermission> {

    List<RolePermission> getByRole(Role role);

    void synchronize(Role role, Collection<Permission> permissions);
}
