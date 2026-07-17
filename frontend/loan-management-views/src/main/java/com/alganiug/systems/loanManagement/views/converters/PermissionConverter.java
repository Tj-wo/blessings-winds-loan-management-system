package com.alganiug.systems.loanManagement.views.converters;

import com.alganiug.systems.loanManagement.core.services.security.PermissionService;
import com.alganiug.systems.loanManagement.models.security.Permission;

import javax.faces.convert.FacesConverter;

@FacesConverter("permissionConverter")
public class PermissionConverter extends EntityConverter<Permission> {

    public PermissionConverter() {
        super("#{permissionServiceImpl}", PermissionService.class);
    }
}
