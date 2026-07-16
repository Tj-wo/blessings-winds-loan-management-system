package com.alganiug.systems.loanManagement.core.services.security.impl;

import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.security.RolePermissionService;
import com.alganiug.systems.loanManagement.models.security.RolePermission;
import org.springframework.stereotype.Service;

@Service
public class RolePermissionServiceImpl extends GenericServiceImpl<RolePermission> implements RolePermissionService {

    public RolePermissionServiceImpl() {
        super(RolePermission.class);
    }

    @Override
    protected void validate(RolePermission rolePermission) {
        requirePresent(rolePermission.getRole(), "Role");
        requirePresent(rolePermission.getPermission(), "Permission");
    }
}
