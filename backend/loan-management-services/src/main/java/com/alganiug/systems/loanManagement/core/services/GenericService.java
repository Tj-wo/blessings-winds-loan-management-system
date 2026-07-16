package com.alganiug.systems.loanManagement.core.services;

import com.alganiug.systems.loanManagement.models.base.BaseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GenericService<T extends BaseEntity> {

    T saveInstance(T entityInstance);

    Optional<T> getInstanceById(UUID id);

    List<T> getInstances(int offset, int limit);

    long countInstances();

    List<T> getAllInstances();

    void deleteInstance(T entityInstance);
}
