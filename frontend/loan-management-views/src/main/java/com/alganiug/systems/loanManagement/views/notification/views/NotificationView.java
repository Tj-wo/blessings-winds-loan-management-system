package com.alganiug.systems.loanManagement.views.notification.views;

import com.alganiug.systems.loanManagement.core.services.notification.NotificationService;
import com.alganiug.systems.loanManagement.models.notification.Notification;
import com.alganiug.systems.loanManagement.models.security.User;
import com.alganiug.systems.loanManagement.views.EntityView;
import com.alganiug.systems.loanManagement.views.controllers.AuthenticationController;
import com.alganiug.systems.loanManagement.views.dialogs.MessageComposer;
import com.alganiug.systems.loanManagement.views.dialogs.UserMessageResolver;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.UUID;

@ManagedBean(name = "notificationView")
@ViewScoped
public class NotificationView extends EntityView<Notification> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{notificationServiceImpl}")
    private NotificationService service;

    @ManagedProperty(value = "#{authenticationController}")
    private AuthenticationController authenticationController;

    @Override
    protected NotificationService getService() { return service; }

    @Override
    public void reload() {
        User user = currentUser();
        if (user != null) {
            setRecords(service.getForUser(user.getId(), companyId(user)));
            return;
        }
        super.reload();
    }

    public long getUnreadCount() {
        User user = currentUser();
        return user == null ? 0 : service.countUnreadForUser(user.getId(), companyId(user));
    }

    public void markAsRead(Notification notification) {
        User user = currentUser();
        if (user == null || notification == null) return;
        try {
            service.markAsRead(notification.getId(), user.getId(), companyId(user));
            reload();
            MessageComposer.info("Notification read", "The notification was marked as read.");
        } catch (RuntimeException exception) {
            MessageComposer.error("Unable to update notification", UserMessageResolver.resolve(exception));
        }
    }

    public void markAllAsRead() {
        User user = currentUser();
        if (user == null) return;
        try {
            int updated = service.markAllAsRead(user.getId(), companyId(user));
            reload();
            MessageComposer.info("Notifications read", updated == 1
                    ? "1 notification was marked as read."
                    : updated + " notifications were marked as read.");
        } catch (RuntimeException exception) {
            MessageComposer.error("Unable to update notifications", UserMessageResolver.resolve(exception));
        }
    }

    private User currentUser() {
        return authenticationController == null ? null : authenticationController.getLoggedInUser();
    }

    private UUID companyId(User user) {
        return user.getCompany() == null ? null : user.getCompany().getId();
    }

    public void setAuthenticationController(AuthenticationController authenticationController) {
        this.authenticationController = authenticationController;
    }

    public void setService(NotificationService service) { this.service = service; }
}