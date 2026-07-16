package com.alganiug.systems.loanManagement.core.services.loan.impl;

import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.loan.PenaltyChargeService;
import com.alganiug.systems.loanManagement.models.loan.PenaltyCharge;
import org.springframework.stereotype.Service;

@Service
public class PenaltyChargeServiceImpl extends GenericServiceImpl<PenaltyCharge> implements PenaltyChargeService {
    public PenaltyChargeServiceImpl() {
        super(PenaltyCharge.class);
    }
}
