package com.alganiug.systems.loanManagement.views.security.views;

import com.alganiug.systems.loanManagement.core.services.security.UserService;
import com.alganiug.systems.loanManagement.models.security.User;
import com.alganiug.systems.loanManagement.views.EntityView;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "userView")
@ViewScoped
public class UserView extends EntityView<User> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{userServiceImpl}")
    private UserService service;

    @Override
    protected UserService getService() {
        return service;
    }

    public void setService(UserService service) {
        this.service = service;
    }
}
