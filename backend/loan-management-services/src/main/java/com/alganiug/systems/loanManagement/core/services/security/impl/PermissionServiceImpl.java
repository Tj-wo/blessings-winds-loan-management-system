package com.alganiug.systems.loanManagement.core.services.security.impl;

import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.security.PermissionService;
import com.alganiug.systems.loanManagement.models.security.Permission;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl extends GenericServiceImpl<Permission> implements PermissionService {

    public PermissionServiceImpl() {
        super(Permission.class);
    }

    @Override
    protected void validate(Permission permission) {
        requireText(permission.getCode(), "Permission code");
        requireText(permission.getName(), "Permission name");
    }
}
