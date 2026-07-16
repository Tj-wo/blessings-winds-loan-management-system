package com.alganiug.systems.loanManagement.views.notification.views;

import com.alganiug.systems.loanManagement.core.services.notification.NotificationService;
import com.alganiug.systems.loanManagement.models.notification.Notification;
import com.alganiug.systems.loanManagement.views.EntityView;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "notificationView")
@ViewScoped
public class NotificationView extends EntityView<Notification> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{notificationServiceImpl}")
    private NotificationService service;

    @Override
    protected NotificationService getService() {
        return service;
    }

    public void setService(NotificationService service) {
        this.service = service;
    }
}
