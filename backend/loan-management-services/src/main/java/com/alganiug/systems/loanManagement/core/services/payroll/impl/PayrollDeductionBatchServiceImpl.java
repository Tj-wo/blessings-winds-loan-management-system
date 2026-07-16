package com.alganiug.systems.loanManagement.core.services.payroll.impl;

import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.payroll.PayrollDeductionBatchService;
import com.alganiug.systems.loanManagement.models.payroll.PayrollDeductionBatch;
import org.springframework.stereotype.Service;

@Service
public class PayrollDeductionBatchServiceImpl extends GenericServiceImpl<PayrollDeductionBatch>
        implements PayrollDeductionBatchService {

    public PayrollDeductionBatchServiceImpl() {
        super(PayrollDeductionBatch.class);
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
