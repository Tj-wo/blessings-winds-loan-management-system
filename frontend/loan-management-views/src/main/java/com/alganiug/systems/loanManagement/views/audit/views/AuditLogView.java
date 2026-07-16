package com.alganiug.systems.loanManagement.views.audit.views;

import com.alganiug.systems.loanManagement.core.services.audit.AuditLogService;
import com.alganiug.systems.loanManagement.models.audit.AuditLog;
import com.alganiug.systems.loanManagement.views.EntityView;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "auditLogView")
@ViewScoped
public class AuditLogView extends EntityView<AuditLog> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{auditLogServiceImpl}")
    private AuditLogService service;

    @Override
    protected AuditLogService getService() {
        return service;
    }

    public void setService(AuditLogService service) {
        this.service = service;
    }
}
