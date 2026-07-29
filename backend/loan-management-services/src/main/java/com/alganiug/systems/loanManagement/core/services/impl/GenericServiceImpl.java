package com.alganiug.systems.loanManagement.core.services.impl;

import com.alganiug.systems.loanManagement.core.services.GenericService;
import com.alganiug.systems.loanManagement.core.services.audit.AuditContext;
import com.alganiug.systems.loanManagement.models.audit.AuditLog;
import com.alganiug.systems.loanManagement.models.security.User;
import com.alganiug.systems.loanManagement.core.services.ServiceOperationException;
import com.alganiug.systems.loanManagement.core.services.ServiceValidationException;
import com.alganiug.systems.loanManagement.models.base.BaseEntity;
import com.alganiug.systems.loanManagement.models.constants.RecordStatus;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
public abstract class GenericServiceImpl<T extends BaseEntity> implements GenericService<T> {

    @PersistenceContext
    protected EntityManager entityManager;

    private final Class<T> entityType;

    protected GenericServiceImpl(Class<T> entityType) {
        this.entityType = entityType;
    }

    @Override
    public T saveInstance(T entityInstance) {
        requireEntity(entityInstance);
        validate(entityInstance);

        if (entityInstance.getId() == null) {
            entityManager.persist(entityInstance);
            return entityInstance;
        }

        return entityManager.merge(entityInstance);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<T> getInstanceById(UUID id) {
        if (id == null) {
            return Optional.empty();
        }

        T entity = entityManager.find(entityType, id);
        return entity == null || entity.getRecordStatus() == RecordStatus.DELETED ? Optional.empty()
                : Optional.of(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> getInstances(int offset, int limit) {
        if (offset < 0) {
            throw new ServiceValidationException("Offset cannot be negative");
        }
        if (limit < 1) {
            throw new ServiceValidationException("Limit must be greater than zero");
        }

        return activeQuery().setFirstResult(offset).setMaxResults(limit).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public long countInstances() {
        String query = "select count(entity) from " + entityType.getSimpleName()
                + " entity where entity.recordStatus = :recordStatus";
        return entityManager.createQuery(query, Long.class).setParameter("recordStatus", RecordStatus.ACTIVE)
                .getSingleResult();
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> getAllInstances() {
        return activeQuery().getResultList();
    }

    @Override
    public void deleteInstance(T entityInstance) {
        requireEntity(entityInstance);
        if (entityInstance.getId() == null) {
            throw new ServiceOperationException("An unsaved entity cannot be deleted");
        }
        if (!isDeletable(entityInstance)) {
            throw new ServiceOperationException("This instance cannot be deleted");
        }

        T managedEntity = entityManager.contains(entityInstance) ? entityInstance : entityManager.merge(entityInstance);
        recordAudit("DELETE", managedEntity);
        entityManager.remove(managedEntity);
    }

    protected void recordAudit(String action, BaseEntity entity) {
        if (entity == null || entity instanceof AuditLog || entity.getId() == null) {
            return;
        }

        AuditLog auditLog = new AuditLog();
        UUID actorId = AuditContext.getActorId();
        if (actorId != null) {
            auditLog.setActor(entityManager.getReference(User.class, actorId));
        }
        auditLog.setAction(action);
        auditLog.setEntityType(entity.getClass().getSimpleName());
        auditLog.setEntityId(entity.getId().toString());
        auditLog.setNewValue("recordStatus=" + entity.getRecordStatus() + ", version=" + entity.getVersion());
        auditLog.setComment(action + " performed through the application");
        auditLog.setIpAddress(AuditContext.getIpAddress());
        auditLog.setEventTime(LocalDateTime.now());
        entityManager.persist(auditLog);
    }
    protected void validate(T entityInstance) {
        // Domain service implementations override this hook.
    }

    protected boolean isDeletable(T entityInstance) {
        return true;
    }

    protected void requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new ServiceValidationException(fieldName + " is required");
        }
    }

    protected void requirePresent(Object value, String fieldName) {
        if (value == null) {
            throw new ServiceValidationException(fieldName + " is required");
        }
    }

    protected void requirePositive(BigDecimal value, String fieldName) {
        requirePresent(value, fieldName);
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceValidationException(fieldName + " must be greater than zero");
        }
    }

    protected void requireNonNegative(BigDecimal value, String fieldName) {
        requirePresent(value, fieldName);
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new ServiceValidationException(fieldName + " cannot be negative");
        }
    }

    protected boolean sameEntity(BaseEntity first, BaseEntity second) {
        if (first == second) {
            return true;
        }
        return first != null && second != null && first.getId() != null && first.getId().equals(second.getId());
    }

    protected UUID requireActorId() {
        UUID actorId = AuditContext.getActorId();
        if (actorId == null) throw new ServiceOperationException("Authentication is required");
        return actorId;
    }

    protected boolean actorHasRole(String roleName) {
        UUID actorId = requireActorId();
        return entityManager.createQuery(
                        "select count(role) from User actor join actor.roles role where actor.id = :actorId "
                                + "and role.name = :roleName and actor.recordStatus = :recordStatus", Long.class)
                .setParameter("actorId", actorId).setParameter("roleName", roleName)
                .setParameter("recordStatus", RecordStatus.ACTIVE).getSingleResult() > 0;
    }

    protected boolean actorHasPermission(String permissionCode) {
        UUID actorId = requireActorId();
        return entityManager.createQuery(
                        "select count(permission) from User actor join actor.roles role "
                                + "join role.rolePermissions assignment join assignment.permission permission "
                                + "where actor.id = :actorId and permission.code = :permissionCode "
                                + "and actor.recordStatus = :recordStatus and role.recordStatus = :recordStatus "
                                + "and assignment.recordStatus = :recordStatus and permission.recordStatus = :recordStatus", Long.class)
                .setParameter("actorId", actorId).setParameter("permissionCode", permissionCode)
                .setParameter("recordStatus", RecordStatus.ACTIVE).getSingleResult() > 0;
    }

    protected UUID actorEmployeeId() {
        return entityManager.createQuery("select actor.employee.id from User actor where actor.id = :actorId", UUID.class)
                .setParameter("actorId", requireActorId()).getResultStream().findFirst().orElse(null);
    }

    protected UUID actorCompanyId() {
        return entityManager.createQuery("select actor.company.id from User actor where actor.id = :actorId", UUID.class)
                .setParameter("actorId", requireActorId()).getResultStream().findFirst().orElse(null);
    }
    private void requireEntity(T entityInstance) {
        if (entityInstance == null) {
            throw new ServiceValidationException("Entity instance is required");
        }
    }

    private TypedQuery<T> activeQuery() {
        String query = "select entity from " + entityType.getSimpleName()
                + " entity where entity.recordStatus = :recordStatus order by entity.createdAt desc";
        return entityManager.createQuery(query, entityType).setParameter("recordStatus", RecordStatus.ACTIVE);
    }
}
