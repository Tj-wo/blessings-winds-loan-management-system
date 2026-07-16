package com.alganiug.systems.loanManagement.views.security.views;

import com.alganiug.systems.loanManagement.core.services.security.RoleService;
import com.alganiug.systems.loanManagement.models.security.Role;
import com.alganiug.systems.loanManagement.views.EntityView;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "roleView")
@ViewScoped
public class RoleView extends EntityView<Role> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{roleServiceImpl}")
    private RoleService service;

    @Override
    protected RoleService getService() {
        return service;
    }

    public void setService(RoleService service) {
        this.service = service;
    }
}
