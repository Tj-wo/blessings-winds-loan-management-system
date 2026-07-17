package com.alganiug.systems.loanManagement.views.security.dialogs;

import com.alganiug.systems.loanManagement.views.navigation.LoanManagementHyperLinks;
import com.alganiug.systems.loanManagement.core.services.security.UserService;
import com.alganiug.systems.loanManagement.models.security.User;
import com.alganiug.systems.loanManagement.views.dialogs.DialogForm;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "userFormDialog")
@ViewScoped
public class UserFormDialog extends DialogForm<User> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{userServiceImpl}")
    private UserService service;

    public UserFormDialog() {
        super(LoanManagementHyperLinks.USER_FORM_DIALOG, 700, 550);
        resetModal();
    }

    @Override
    protected UserService getService() {
        return service;
    }

    public void setService(UserService service) {
        this.service = service;
    }

    @Override
    public void resetModal() {
        model = new User();
        editing = false;
    }
}
