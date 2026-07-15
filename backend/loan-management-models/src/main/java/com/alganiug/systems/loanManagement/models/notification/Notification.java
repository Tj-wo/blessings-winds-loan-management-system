package com.alganiug.systems.loanManagement.models.notification;

import com.alganiug.systems.loanManagement.models.base.BaseEntity;
import com.alganiug.systems.loanManagement.models.constants.*;
import com.alganiug.systems.loanManagement.models.security.Role;
import com.alganiug.systems.loanManagement.models.security.User;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications", indexes = {
        @Index(name = "idx_notification_recipient_read", columnList = "recipient_id,read_at"),
        @Index(name = "idx_notification_audience", columnList = "audience_role_id") })
public class Notification extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id")

    private User recipient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audience_role_id")

    private Role audienceRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false, length = 20)

    private NotificationChannel channel = NotificationChannel.IN_APP;

    @Column(name = "title", nullable = false, length = 200)

    private String title;

    @Column(name = "message", nullable = false, length = 2000)

    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)

    private NotificationStatus status = NotificationStatus.PENDING;

    @Column(name = "sent_at")

    private LocalDateTime sentAt;

    @Column(name = "read_at")

    private LocalDateTime readAt;

    @Column(name = "failure_reason", length = 500)

    private String failureReason;

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User v) {
        recipient = v;
    }

    public Role getAudienceRole() {
        return audienceRole;
    }

    public void setAudienceRole(Role v) {
        audienceRole = v;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public void setChannel(NotificationChannel v) {
        channel = v;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String v) {
        title = v;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String v) {
        message = v;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus v) {
        status = v;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime v) {
        sentAt = v;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime v) {
        readAt = v;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String v) {
        failureReason = v;
    }
}
