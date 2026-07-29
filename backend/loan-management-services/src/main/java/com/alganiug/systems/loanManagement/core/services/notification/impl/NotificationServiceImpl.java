package com.alganiug.systems.loanManagement.core.services.notification.impl;

import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.notification.NotificationService;
import com.alganiug.systems.loanManagement.models.constants.NotificationStatus;
import com.alganiug.systems.loanManagement.models.constants.RecordStatus;
import com.alganiug.systems.loanManagement.models.notification.Notification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationServiceImpl extends GenericServiceImpl<Notification> implements NotificationService {

    private static final String VISIBLE_TO_USER = "recipient.id = :userId";

    public NotificationServiceImpl() {
        super(Notification.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getForUser(UUID userId, UUID companyId) {
        if (userId == null) return Collections.emptyList();
        return entityManager.createQuery(
                        "select distinct notification from Notification notification "
                                + "left join notification.recipient recipient "
                                + "left join notification.audienceRole audienceRole "
                                + "where notification.recordStatus = :recordStatus and " + VISIBLE_TO_USER
                                + " order by notification.createdAt desc", Notification.class)
                .setParameter("recordStatus", RecordStatus.ACTIVE)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public long countUnreadForUser(UUID userId, UUID companyId) {
        if (userId == null) return 0;
        return entityManager.createQuery(
                        "select count(distinct notification.id) from Notification notification "
                                + "left join notification.recipient recipient "
                                + "left join notification.audienceRole audienceRole "
                                + "where notification.recordStatus = :recordStatus "
                                + "and notification.readAt is null and " + VISIBLE_TO_USER, Long.class)
                .setParameter("recordStatus", RecordStatus.ACTIVE)
                .setParameter("userId", userId)
                .getSingleResult();
    }

    @Override
    @Transactional
    public void markAsRead(UUID notificationId, UUID userId, UUID companyId) {
        if (notificationId == null || userId == null) return;
        List<Notification> matches = entityManager.createQuery(
                        "select distinct notification from Notification notification "
                                + "left join notification.recipient recipient "
                                + "left join notification.audienceRole audienceRole "
                                + "where notification.id = :notificationId "
                                + "and notification.recordStatus = :recordStatus and " + VISIBLE_TO_USER,
                        Notification.class)
                .setParameter("notificationId", notificationId)
                .setParameter("recordStatus", RecordStatus.ACTIVE)
                .setParameter("userId", userId)
                .getResultList();
        if (matches.isEmpty()) throw new IllegalArgumentException("Notification not found.");
        Notification notification = matches.get(0);
        if (notification.getReadAt() == null) {
            notification.setReadAt(LocalDateTime.now());
            notification.setStatus(NotificationStatus.READ);
        }
    }

    @Override
    @Transactional
    public int markAllAsRead(UUID userId, UUID companyId) {
        if (userId == null) return 0;
        List<Notification> notifications = getForUser(userId, companyId);
        int updated = 0;
        LocalDateTime now = LocalDateTime.now();
        for (Notification notification : notifications) {
            if (notification.getReadAt() == null) {
                notification.setReadAt(now);
                notification.setStatus(NotificationStatus.READ);
                updated++;
            }
        }
        return updated;
    }
}