package com.alganiug.systems.loanManagement.core.services.security;

import com.alganiug.systems.loanManagement.core.services.GenericService;
import com.alganiug.systems.loanManagement.models.security.User;

import java.util.Set;
import java.util.UUID;

public interface UserService extends GenericService<User> {

    boolean hasPermission(UUID userId, String permissionCode);

    Set<String> getPermissionCodes(UUID userId);

    boolean hasRole(UUID userId, String roleName);
}
