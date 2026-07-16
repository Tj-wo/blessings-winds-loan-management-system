package com.alganiug.systems.loanManagement.core.services.loan.impl;

import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.loan.LoanApprovalService;
import com.alganiug.systems.loanManagement.models.loan.LoanApproval;
import org.springframework.stereotype.Service;

@Service
public class LoanApprovalServiceImpl extends GenericServiceImpl<LoanApproval> implements LoanApprovalService {
    public LoanApprovalServiceImpl() {
        super(LoanApproval.class);
    }
}
