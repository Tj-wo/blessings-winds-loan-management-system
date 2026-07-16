package com.alganiug.systems.loanManagement.core.services.settings.impl;

import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.settings.SystemSettingService;
import com.alganiug.systems.loanManagement.models.settings.SystemSetting;
import org.springframework.stereotype.Service;

@Service
public class SystemSettingServiceImpl extends GenericServiceImpl<SystemSetting> implements SystemSettingService {
    public SystemSettingServiceImpl() {
        super(SystemSetting.class);
    }
}
