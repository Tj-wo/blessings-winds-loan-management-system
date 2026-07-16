package com.alganiug.systems.loanManagement.core.services.loan.impl;

import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.loan.RepaymentScheduleItemService;
import com.alganiug.systems.loanManagement.models.loan.RepaymentScheduleItem;
import org.springframework.stereotype.Service;

@Service
public class RepaymentScheduleItemServiceImpl extends GenericServiceImpl<RepaymentScheduleItem>
        implements RepaymentScheduleItemService {
    public RepaymentScheduleItemServiceImpl() {
        super(RepaymentScheduleItem.class);
    }
}
