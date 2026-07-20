package com.alganiug.systems.loanManagement.views.controllers;

import com.alganiug.systems.loanManagement.core.services.security.UserService;
import com.alganiug.systems.loanManagement.views.dialogs.MessageComposer;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

@ManagedBean(name = "passwordResetController")
@ViewScoped
public class PasswordResetController implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(PasswordResetController.class.getName());

    @ManagedProperty(value = "#{userServiceImpl}")
    private UserService userService;

    private String usernameOrEmail;
    private String newPassword;
    private String confirmPassword;

    public void requestReset() {
        if (userService.accountExists(usernameOrEmail)) {
            MessageComposer.info("Account found",
                    "You can continue to reset your password using your username or email.");
        } else {
            MessageComposer.error("Account not found", "No account matches that username or email.");
        }
    }

    public void resetPassword() {
        try {
            if (newPassword == null || !newPassword.equals(confirmPassword)) {
                throw new IllegalArgumentException("The password confirmation does not match");
            }
            userService.resetPassword(usernameOrEmail, newPassword);
            newPassword = null;
            confirmPassword = null;
            MessageComposer.info("Password updated", "Your password was changed successfully. You can now sign in.");
        } catch (RuntimeException exception) {
            LOGGER.log(Level.WARNING, "Password reset failed", exception);
            MessageComposer.error("Password reset failed", exception.getMessage());
        }
    }

    public String getUsernameOrEmail() { return usernameOrEmail; }
    public void setUsernameOrEmail(String usernameOrEmail) { this.usernameOrEmail = usernameOrEmail; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
    public void setUserService(UserService userService) { this.userService = userService; }
}