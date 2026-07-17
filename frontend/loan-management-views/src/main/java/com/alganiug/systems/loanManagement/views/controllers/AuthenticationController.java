package com.alganiug.systems.loanManagement.views.controllers;

import com.alganiug.systems.loanManagement.core.services.security.UserService;
import com.alganiug.systems.loanManagement.models.security.User;
import com.alganiug.systems.loanManagement.views.navigation.LoanManagementHyperLinks;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Optional;

@ManagedBean(name = "authenticationController")
@SessionScoped
public class AuthenticationController implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{userServiceImpl}")
    private UserService userService;

    private String username;
    private String password;
    private User loggedInUser;

    public String login() {
        Optional<User> authenticatedUser = userService.authenticate(username, password);
        if (!authenticatedUser.isPresent()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sign in failed", "Invalid username or password"));
            return null;
        }

        loggedInUser = authenticatedUser.get();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loggedInUser", loggedInUser);
        password = null;
        return LoanManagementHyperLinks.DASHBOARD;
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return LoanManagementHyperLinks.LOGIN;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
