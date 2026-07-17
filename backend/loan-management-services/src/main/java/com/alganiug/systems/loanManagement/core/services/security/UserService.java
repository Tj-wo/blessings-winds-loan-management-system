package com.alganiug.systems.loanManagement.core.services.security;

import com.alganiug.systems.loanManagement.core.services.GenericService;
import com.alganiug.systems.loanManagement.models.security.User;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserService extends GenericService<User> {

    boolean hasPermission(UUID userId, String permissionCode);

    Optional<User> authenticate(String username, String password);

    Set<String> getPermissionCodes(UUID userId);

    boolean hasRole(UUID userId, String roleName);
}
