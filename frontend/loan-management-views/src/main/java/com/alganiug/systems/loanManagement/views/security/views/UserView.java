package com.alganiug.systems.loanManagement.views.security.views;

import com.alganiug.systems.loanManagement.core.services.security.UserService;
import com.alganiug.systems.loanManagement.models.security.User;
import com.alganiug.systems.loanManagement.models.constants.AccountStatus;
import com.alganiug.systems.loanManagement.views.dialogs.MessageComposer;
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

    public void activate(User user) {
        service.updateAccountStatus(user, AccountStatus.ACTIVE);
        reload();
        MessageComposer.info("Account activated", user.getUsername() + " can now sign in.");
    }

    public void deactivate(User user) {
        service.updateAccountStatus(user, AccountStatus.DISABLED);
        reload();
        MessageComposer.info("Account deactivated", user.getUsername() + " can no longer sign in.");
    }
    public void setService(UserService service) {
        this.service = service;
    }
}
