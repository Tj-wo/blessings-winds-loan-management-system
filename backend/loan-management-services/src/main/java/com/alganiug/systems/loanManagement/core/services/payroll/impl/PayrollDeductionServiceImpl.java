package com.alganiug.systems.loanManagement.core.services.payroll.impl;

import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.ServiceValidationException;
import com.alganiug.systems.loanManagement.core.services.payroll.PayrollDeductionService;
import com.alganiug.systems.loanManagement.models.payroll.PayrollDeduction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class PayrollDeductionServiceImpl extends GenericServiceImpl<PayrollDeduction>
        implements PayrollDeductionService {

    public PayrollDeductionServiceImpl() {
        super(PayrollDeduction.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PayrollDeduction> getForCompany(UUID companyId) {
        if (companyId == null) return Collections.emptyList();
        return entityManager.createQuery("select payrolldeduction from PayrollDeduction payrolldeduction where payrolldeduction.batch.company.id = :companyId and payrolldeduction.recordStatus = com.alganiug.systems.loanManagement.models.constants.RecordStatus.ACTIVE order by payrolldeduction.createdAt desc", PayrollDeduction.class)
                .setParameter("companyId", companyId).getResultList();
    }

    @Override
    protected void validate(PayrollDeduction deduction) {
        requirePresent(deduction.getBatch(), "Payroll batch");
        requirePresent(deduction.getEmployee(), "Employee");
        requirePresent(deduction.getLoan(), "Loan");
        if (!sameEntity(deduction.getBatch().getCompany(), deduction.getEmployee().getCompany())) {
            throw new ServiceValidationException("Payroll batch and employee must belong to the same company");
        }
        if (!sameEntity(deduction.getEmployee(), deduction.getLoan().getEmployee())) {
            throw new ServiceValidationException("Payroll deduction loan must belong to the employee");
        }
        requireNonNegative(deduction.getExpectedAmount(), "Expected deduction amount");
        requireNonNegative(deduction.getDeductedAmount(), "Deducted amount");
        requirePresent(deduction.getStatus(), "Reconciliation status");
    }
}
