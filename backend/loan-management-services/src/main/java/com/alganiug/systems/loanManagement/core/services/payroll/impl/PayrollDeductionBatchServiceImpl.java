package com.alganiug.systems.loanManagement.core.services.payroll.impl;

import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.payroll.PayrollDeductionBatchService;
import com.alganiug.systems.loanManagement.models.payroll.PayrollDeductionBatch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class PayrollDeductionBatchServiceImpl extends GenericServiceImpl<PayrollDeductionBatch>
        implements PayrollDeductionBatchService {

    public PayrollDeductionBatchServiceImpl() {
        super(PayrollDeductionBatch.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PayrollDeductionBatch> getForCompany(UUID companyId) {
        if (companyId == null) return Collections.emptyList();
        return entityManager.createQuery("select payrolldeductionbatch from PayrollDeductionBatch payrolldeductionbatch where payrolldeductionbatch.company.id = :companyId and payrolldeductionbatch.recordStatus = com.alganiug.systems.loanManagement.models.constants.RecordStatus.ACTIVE order by payrolldeductionbatch.createdAt desc", PayrollDeductionBatch.class)
                .setParameter("companyId", companyId).getResultList();
    }

    @Override
    protected void validate(PayrollDeductionBatch batch) {
        requirePresent(batch.getCompany(), "Company");
        requirePresent(batch.getPayrollPeriod(), "Payroll period");
        requireText(batch.getBatchReference(), "Batch reference");
        requireText(batch.getOriginalFilename(), "Original filename");
        requireNonNegative(batch.getExpectedTotal(), "Expected total");
        requireNonNegative(batch.getReceivedTotal(), "Received total");
        requirePresent(batch.getUploadedBy(), "Uploader");
        requirePresent(batch.getUploadedAt(), "Upload time");
        requirePresent(batch.getStatus(), "Payroll batch status");
    }
}
