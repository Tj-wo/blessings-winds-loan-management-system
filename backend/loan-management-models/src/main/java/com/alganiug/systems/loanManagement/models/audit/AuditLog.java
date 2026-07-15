package com.alganiug.systems.loanManagement.models.audit;

import com.alganiug.systems.loanManagement.models.base.BaseEntity;
import com.alganiug.systems.loanManagement.models.security.User;
import org.hibernate.annotations.Immutable;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Immutable
@Table(name = "audit_logs", indexes = { @Index(name = "idx_audit_entity", columnList = "entity_type,entity_id"),
        @Index(name = "idx_audit_actor_date", columnList = "actor_id,event_time") })
public class AuditLog extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id")

    private User actor;

    @Column(name = "action", nullable = false, length = 100)

    private String action;

    @Column(name = "entity_type", nullable = false, length = 120)

    private String entityType;

    @Column(name = "entity_id", nullable = false, length = 36)

    private String entityId;

    @Lob
    @Column(name = "old_value")

    private String oldValue;

    @Lob
    @Column(name = "new_value")

    private String newValue;

    @Column(name = "comment", length = 1000)

    private String comment;

    @Column(name = "ip_address", length = 45)

    private String ipAddress;

    @Column(name = "event_time", nullable = false)

    private LocalDateTime eventTime;

    public User getActor() {
        return actor;
    }

    public void setActor(User v) {
        actor = v;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String v) {
        action = v;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String v) {
        entityType = v;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String v) {
        entityId = v;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String v) {
        oldValue = v;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String v) {
        newValue = v;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String v) {
        comment = v;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String v) {
        ipAddress = v;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime v) {
        eventTime = v;
    }
}
