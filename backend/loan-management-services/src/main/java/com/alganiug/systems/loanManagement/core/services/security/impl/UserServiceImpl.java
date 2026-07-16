package com.alganiug.systems.loanManagement.core.services.security.impl;

import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.ServiceValidationException;
import com.alganiug.systems.loanManagement.core.services.security.UserService;
import com.alganiug.systems.loanManagement.models.security.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends GenericServiceImpl<User> implements UserService {

    public UserServiceImpl() {
        super(User.class);
    }

    @Override
    protected void validate(User user) {
        requireText(user.getUsername(), "Username");
        requireText(user.getPasswordHash(), "Password hash");
        requireText(user.getDisplayName(), "Display name");
        requirePresent(user.getAccountStatus(), "Account status");
        if (user.getEmployee() != null && user.getCompany() == null) {
            throw new ServiceValidationException("An employee user must have a company");
        }
        if (user.getEmployee() != null && !sameEntity(user.getCompany(), user.getEmployee().getCompany())) {
            throw new ServiceValidationException("User company must match the employee company");
        }
        if (user.getRoles().isEmpty()) {
            throw new ServiceValidationException("At least one role is required");
        }
    }
}
