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
    @ManagedProperty(value = "#{userServiceImpl}") private UserService userService;
    private String email;
    private String otp;
    private String newPassword;
    private String confirmPassword;
    private boolean otpSent;

    public void requestOtp() {
        try {
            userService.requestPasswordResetOtp(email);
            otpSent = true;
            MessageComposer.info("Verification code sent", "Check your email. The six-digit code expires in 10 minutes.");
        } catch (RuntimeException exception) {
            LOGGER.log(Level.WARNING, "OTP request failed", exception);
            MessageComposer.error("Unable to send code", exception.getMessage());
        }
    }
    public void resetPassword() {
        try {
            if (newPassword == null || !newPassword.equals(confirmPassword)) throw new IllegalArgumentException("The password confirmation does not match");
            userService.resetPasswordWithOtp(email, otp, newPassword);
            otp = null; newPassword = null; confirmPassword = null; otpSent = false;
            MessageComposer.info("Password updated", "Your verification code was accepted. You can now sign in.");
        } catch (RuntimeException exception) {
            LOGGER.log(Level.WARNING, "OTP password reset failed", exception);
            MessageComposer.error("Password reset failed", exception.getMessage());
        }
    }
    public String getEmail() { return email; }
    public void setEmail(String value) { email = value; }
    public String getOtp() { return otp; }
    public void setOtp(String value) { otp = value; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String value) { newPassword = value; }
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String value) { confirmPassword = value; }
    public boolean isOtpSent() { return otpSent; }
    public void setUserService(UserService value) { userService = value; }
}