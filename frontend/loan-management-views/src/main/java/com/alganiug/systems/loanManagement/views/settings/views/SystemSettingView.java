package com.alganiug.systems.loanManagement.views.settings.views;

import com.alganiug.systems.loanManagement.core.services.settings.SystemSettingService;
import com.alganiug.systems.loanManagement.models.settings.SystemSetting;
import com.alganiug.systems.loanManagement.views.EntityView;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "systemSettingView")
@ViewScoped
public class SystemSettingView extends EntityView<SystemSetting> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{systemSettingServiceImpl}")
    private SystemSettingService service;

    @Override
    protected SystemSettingService getService() {
        return service;
    }

    public void setService(SystemSettingService service) {
        this.service = service;
    }
}
