package com.alganiug.systems.loanManagement.core.services.security.impl;

import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.security.RoleService;
import com.alganiug.systems.loanManagement.models.security.Role;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends GenericServiceImpl<Role> implements RoleService {

    public RoleServiceImpl() {
        super(Role.class);
    }

    @Override
    protected void validate(Role role) {
        requireText(role.getName(), "Role name");
    }
}
