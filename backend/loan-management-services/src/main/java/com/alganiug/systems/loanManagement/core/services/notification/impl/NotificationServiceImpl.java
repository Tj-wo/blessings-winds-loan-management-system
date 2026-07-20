package com.alganiug.systems.loanManagement.core.services.notification.impl;

import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.notification.NotificationService;
import com.alganiug.systems.loanManagement.models.constants.RecordStatus;
import com.alganiug.systems.loanManagement.models.notification.Notification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationServiceImpl extends GenericServiceImpl<Notification> implements NotificationService {

    public NotificationServiceImpl() {
        super(Notification.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getForUser(UUID userId, UUID companyId) {
        if (userId == null) {
            return Collections.emptyList();
        }

        return entityManager.createQuery(
                        "select distinct notification from Notification notification "
                                + "left join notification.recipient recipient "
                                + "left join notification.audienceRole audienceRole "
                                + "where notification.recordStatus = :recordStatus and ("
                                + "recipient.id = :userId "
                                + "or (:companyId is not null and recipient.company.id = :companyId) "
                                + "or audienceRole.id in (select role.id from User user join user.roles role where user.id = :userId)) "
                                + "order by notification.createdAt desc",
                        Notification.class)
                .setParameter("recordStatus", RecordStatus.ACTIVE)
                .setParameter("userId", userId)
                .setParameter("companyId", companyId)
                .getResultList();
    }
}
