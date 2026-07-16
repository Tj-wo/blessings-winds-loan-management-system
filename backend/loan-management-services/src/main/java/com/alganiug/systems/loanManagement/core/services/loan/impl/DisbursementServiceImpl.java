package com.alganiug.systems.loanManagement.core.services.loan.impl;

import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.loan.DisbursementService;
import com.alganiug.systems.loanManagement.models.loan.Disbursement;
import org.springframework.stereotype.Service;

@Service
public class DisbursementServiceImpl extends GenericServiceImpl<Disbursement> implements DisbursementService {
    public DisbursementServiceImpl() {
        super(Disbursement.class);
    }
}
