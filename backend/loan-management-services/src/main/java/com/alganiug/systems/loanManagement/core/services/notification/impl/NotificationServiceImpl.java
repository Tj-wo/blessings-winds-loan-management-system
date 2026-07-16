package com.alganiug.systems.loanManagement.core.services.notification.impl;

import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.notification.NotificationService;
import com.alganiug.systems.loanManagement.models.notification.Notification;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl extends GenericServiceImpl<Notification> implements NotificationService {
    public NotificationServiceImpl() {
        super(Notification.class);
    }
}
