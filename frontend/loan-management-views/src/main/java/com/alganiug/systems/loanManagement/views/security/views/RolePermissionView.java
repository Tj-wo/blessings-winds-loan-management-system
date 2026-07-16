package com.alganiug.systems.loanManagement.views.security.views;

import com.alganiug.systems.loanManagement.core.services.security.RolePermissionService;
import com.alganiug.systems.loanManagement.models.security.RolePermission;
import com.alganiug.systems.loanManagement.views.EntityView;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "rolePermissionView")
@ViewScoped
public class RolePermissionView extends EntityView<RolePermission> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{rolePermissionServiceImpl}")
    private RolePermissionService service;

    @Override
    protected RolePermissionService getService() {
        return service;
    }

    public void setService(RolePermissionService service) {
        this.service = service;
    }
}
