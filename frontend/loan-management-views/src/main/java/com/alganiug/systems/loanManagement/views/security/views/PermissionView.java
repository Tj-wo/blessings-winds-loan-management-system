package com.alganiug.systems.loanManagement.views.security.views;

import com.alganiug.systems.loanManagement.core.services.security.PermissionService;
import com.alganiug.systems.loanManagement.models.security.Permission;
import com.alganiug.systems.loanManagement.views.EntityView;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "permissionView")
@ViewScoped
public class PermissionView extends EntityView<Permission> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{permissionServiceImpl}")
    private PermissionService service;

    @Override
    protected PermissionService getService() {
        return service;
    }

    public void setService(PermissionService service) {
        this.service = service;
    }
}
