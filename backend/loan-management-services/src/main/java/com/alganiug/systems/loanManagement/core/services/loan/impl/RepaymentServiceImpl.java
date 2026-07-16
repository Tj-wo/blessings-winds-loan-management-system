package com.alganiug.systems.loanManagement.core.services.loan.impl;

import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.ServiceValidationException;
import com.alganiug.systems.loanManagement.core.services.loan.RepaymentService;
import com.alganiug.systems.loanManagement.models.loan.Repayment;
import org.springframework.stereotype.Service;

@Service
public class RepaymentServiceImpl extends GenericServiceImpl<Repayment> implements RepaymentService {

    public RepaymentServiceImpl() {
        super(Repayment.class);
    }

    @Override
    protected void validate(Repayment repayment) {
        requirePresent(repayment.getPayrollDeduction(), "Payroll deduction");
        requirePresent(repayment.getLoan(), "Loan");
        if (!sameEntity(repayment.getLoan(), repayment.getPayrollDeduction().getLoan())) {
            throw new ServiceValidationException("Repayment loan must match the payroll deduction loan");
        }
        requirePositive(repayment.getAmount(), "Repayment amount");
        requirePresent(repayment.getPaymentDate(), "Payment date");
        requireText(repayment.getPaymentReference(), "Payment reference");
        requirePresent(repayment.getStatus(), "Repayment status");
    }
}
