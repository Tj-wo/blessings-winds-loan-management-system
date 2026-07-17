package com.alganiug.systems.loanManagement.views.converters;

import com.alganiug.systems.loanManagement.core.services.security.UserService;
import com.alganiug.systems.loanManagement.models.security.User;

import javax.faces.convert.FacesConverter;

@FacesConverter("userConverter")
public class UserConverter extends EntityConverter<User> {

    public UserConverter() {
        super("#{userServiceImpl}", UserService.class);
    }
}
