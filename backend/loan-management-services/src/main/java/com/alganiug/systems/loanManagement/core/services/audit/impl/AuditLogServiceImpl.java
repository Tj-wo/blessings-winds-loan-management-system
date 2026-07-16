package com.alganiug.systems.loanManagement.core.services.audit.impl;

import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.audit.AuditLogService;
import com.alganiug.systems.loanManagement.models.audit.AuditLog;
import org.springframework.stereotype.Service;

@Service
public class AuditLogServiceImpl extends GenericServiceImpl<AuditLog> implements AuditLogService {
    public AuditLogServiceImpl() {
        super(AuditLog.class);
    }
}
