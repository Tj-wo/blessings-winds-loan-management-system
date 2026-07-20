package com.alganiug.systems.loanManagement.views.notification.views;

import com.alganiug.systems.loanManagement.core.services.notification.NotificationService;
import com.alganiug.systems.loanManagement.models.notification.Notification;
import com.alganiug.systems.loanManagement.views.EntityView;
import com.alganiug.systems.loanManagement.views.controllers.AuthenticationController;
import com.alganiug.systems.loanManagement.models.security.User;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "notificationView")
@ViewScoped
public class NotificationView extends EntityView<Notification> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{notificationServiceImpl}")
    private NotificationService service;

    @ManagedProperty(value = "#{authenticationController}")
    private AuthenticationController authenticationController;

    @Override
    protected NotificationService getService() {
        return service;
    }

    @Override
    public void reload() {
        User user = authenticationController == null ? null : authenticationController.getLoggedInUser();
        if (user != null) {
            setRecords(service.getForUser(user.getId(), user.getCompany() == null ? null : user.getCompany().getId()));
            return;
        }
        super.reload();
    }

    public void setAuthenticationController(AuthenticationController authenticationController) {
        this.authenticationController = authenticationController;
    }
    public void setService(NotificationService service) {
        this.service = service;
    }
}
