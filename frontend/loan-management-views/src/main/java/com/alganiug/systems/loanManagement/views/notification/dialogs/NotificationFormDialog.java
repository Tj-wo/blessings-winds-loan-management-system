package com.alganiug.systems.loanManagement.views.notification.dialogs;

import com.alganiug.systems.loanManagement.views.navigation.LoanManagementHyperLinks;
import com.alganiug.systems.loanManagement.core.services.notification.NotificationService;
import com.alganiug.systems.loanManagement.models.notification.Notification;
import com.alganiug.systems.loanManagement.views.dialogs.DialogForm;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "notificationFormDialog")
@ViewScoped
public class NotificationFormDialog extends DialogForm<Notification> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{notificationServiceImpl}")
    private NotificationService service;

    public NotificationFormDialog() {
        super(LoanManagementHyperLinks.NOTIFICATION_FORM_DIALOG, 700, 550);
        resetModal();
    }

    @Override
    protected NotificationService getService() {
        return service;
    }

    public void setService(NotificationService service) {
        this.service = service;
    }

    @Override
    public void resetModal() {
        model = new Notification();
        editing = false;
    }
}
