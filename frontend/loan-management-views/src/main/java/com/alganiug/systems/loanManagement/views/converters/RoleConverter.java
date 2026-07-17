package com.alganiug.systems.loanManagement.views.converters;

import com.alganiug.systems.loanManagement.core.services.security.RoleService;
import com.alganiug.systems.loanManagement.models.security.Role;

import javax.faces.convert.FacesConverter;

@FacesConverter("roleConverter")
public class RoleConverter extends EntityConverter<Role> {

    public RoleConverter() {
        super("#{roleServiceImpl}", RoleService.class);
    }
}
