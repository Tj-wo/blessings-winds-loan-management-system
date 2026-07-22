package com.alganiug.systems.loanManagement.core.services.notification;

import com.alganiug.systems.loanManagement.core.services.GenericService;
import com.alganiug.systems.loanManagement.models.notification.Notification;
import java.util.List;
import java.util.UUID;

public interface NotificationService extends GenericService<Notification> {
    List<Notification> getForUser(UUID userId, UUID companyId);
    long countUnreadForUser(UUID userId, UUID companyId);
    void markAsRead(UUID notificationId, UUID userId, UUID companyId);
    int markAllAsRead(UUID userId, UUID companyId);
}