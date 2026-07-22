package com.alganiug.systems.loanManagement.core.services.payroll.impl;

import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.payroll.PayrollDeductionBatchService;
import com.alganiug.systems.loanManagement.models.payroll.PayrollDeductionBatch;
import com.alganiug.systems.loanManagement.models.payroll.PayrollDeduction;
import com.alganiug.systems.loanManagement.models.loan.Loan;
import com.alganiug.systems.loanManagement.models.constants.DeductionStatus;
import com.alganiug.systems.loanManagement.models.constants.LoanStatus;
import com.alganiug.systems.loanManagement.models.constants.RecordStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;
import java.util.Arrays;

@Service
public class PayrollDeductionBatchServiceImpl extends GenericServiceImpl<PayrollDeductionBatch>
        implements PayrollDeductionBatchService {

    public PayrollDeductionBatchServiceImpl() {
        super(PayrollDeductionBatch.class);
    }

    @Override
    public PayrollDeductionBatch saveInstance(PayrollDeductionBatch batch) {
        boolean creating = batch != null && batch.getId() == null;
        List<Loan> eligibleLoans = creating ? entityManager.createQuery(
                        "select loan from Loan loan join fetch loan.employee where loan.company.id = :companyId "
                                + "and loan.status in :statuses and loan.recordStatus = :recordStatus", Loan.class)
                .setParameter("companyId", batch.getCompany().getId())
                .setParameter("statuses", Arrays.asList(LoanStatus.DISBURSED, LoanStatus.ACTIVE,
                        LoanStatus.LATE, LoanStatus.DEFAULTED))
                .setParameter("recordStatus", RecordStatus.ACTIVE)
                .getResultList() : Collections.emptyList();
        if (creating) {
            BigDecimal expectedTotal = eligibleLoans.stream().map(loan -> dueForMonth(loan))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            batch.setExpectedTotal(expectedTotal);
            batch.setReceivedTotal(BigDecimal.ZERO);
            batch.setRecordCount(eligibleLoans.size());
        }
        PayrollDeductionBatch saved = super.saveInstance(batch);
        if (creating) {
            for (Loan loan : eligibleLoans) {
                PayrollDeduction deduction = new PayrollDeduction();
                deduction.setBatch(saved);
                deduction.setLoan(loan);
                deduction.setEmployee(loan.getEmployee());
                deduction.setExpectedAmount(dueForMonth(loan));
                deduction.setDeductedAmount(BigDecimal.ZERO);
                deduction.setStatus(DeductionStatus.PENDING);
                entityManager.persist(deduction);
            }
            recordAudit("PAYROLL_BATCH_GENERATED", saved);
        }
        return saved;
    }

    private BigDecimal dueForMonth(Loan loan) {
        BigDecimal installment = loan.getInstallmentAmount() == null ? BigDecimal.ZERO : loan.getInstallmentAmount();
        BigDecimal outstanding = loan.getOutstandingBalance() == null ? BigDecimal.ZERO : loan.getOutstandingBalance();
        return installment.min(outstanding);
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
