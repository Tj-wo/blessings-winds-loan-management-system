package com.alganiug.systems.loanManagement.views.settings.dialogs;

import com.alganiug.systems.loanManagement.core.services.settings.SystemSettingService;
import com.alganiug.systems.loanManagement.models.settings.SystemSetting;
import com.alganiug.systems.loanManagement.views.dialogs.DialogForm;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "systemSettingFormDialog")
@ViewScoped
public class SystemSettingFormDialog extends DialogForm<SystemSetting> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{systemSettingServiceImpl}")
    private SystemSettingService service;

    public SystemSettingFormDialog() {
        super("/pages/settings/SystemSettingFormDialog", 700, 550);
    }

    @Override
    protected SystemSettingService getService() {
        return service;
    }

    public void setService(SystemSettingService service) {
        this.service = service;
    }

    @Override
    public void resetModal() {
        model = new SystemSetting();
        editing = false;
    }
}
