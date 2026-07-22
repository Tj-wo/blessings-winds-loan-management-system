package com.alganiug.systems.loanManagement.views.notification.views;

import com.alganiug.systems.loanManagement.core.services.notification.NotificationService;
import com.alganiug.systems.loanManagement.models.security.User;
import com.alganiug.systems.loanManagement.views.controllers.AuthenticationController;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import java.util.UUID;

@ManagedBean(name = "notificationBadgeView")
@RequestScoped
public class NotificationBadgeView {
    @ManagedProperty(value = "#{notificationServiceImpl}")
    private NotificationService service;
    @ManagedProperty(value = "#{authenticationController}")
    private AuthenticationController authenticationController;

    public long getUnreadCount() {
        User user = authenticationController == null ? null : authenticationController.getLoggedInUser();
        if (user == null || service == null) return 0;
        return service.countUnreadForUser(user.getId(), companyId(user));
    }

    private UUID companyId(User user) {
        return user.getCompany() == null ? null : user.getCompany().getId();
    }

    public void setService(NotificationService service) { this.service = service; }
    public void setAuthenticationController(AuthenticationController authenticationController) { this.authenticationController = authenticationController; }
}