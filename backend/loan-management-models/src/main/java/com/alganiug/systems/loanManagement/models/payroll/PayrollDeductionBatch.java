package com.alganiug.systems.loanManagement.models.payroll;

import com.alganiug.systems.loanManagement.models.base.BaseEntity;
import com.alganiug.systems.loanManagement.models.company.Company;
import com.alganiug.systems.loanManagement.models.constants.PayrollStatus;
import com.alganiug.systems.loanManagement.models.security.User;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Entity
@Table(name = "payroll_deduction_batches", uniqueConstraints = @UniqueConstraint(name = "uk_payroll_batch_reference", columnNames = "batch_reference"), indexes = @Index(name = "idx_payroll_company_period", columnList = "company_id,payroll_period"))
public class PayrollDeductionBatch extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)

    private Company company;

    @Column(name = "payroll_period", nullable = false, length = 7)

    private String payrollPeriod;

    @Column(name = "batch_reference", nullable = false, length = 80)

    private String batchReference;

    @Column(name = "original_filename", nullable = false, length = 255)

    private String originalFilename;

    @Column(name = "expected_total", nullable = false, precision = 19, scale = 2)

    private BigDecimal expectedTotal = BigDecimal.ZERO;

    @Column(name = "received_total", nullable = false, precision = 19, scale = 2)

    private BigDecimal receivedTotal = BigDecimal.ZERO;

    @Column(name = "record_count", nullable = false)

    private int recordCount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uploaded_by", nullable = false)

    private User uploadedBy;

    @Column(name = "uploaded_at", nullable = false)

    private LocalDateTime uploadedAt;

    @Column(name = "posted_at")

    private LocalDateTime postedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)

    private PayrollStatus status = PayrollStatus.DRAFT;

    public YearMonth getPayrollPeriod() {
        return payrollPeriod == null ? null : YearMonth.parse(payrollPeriod);
    }

    public void setPayrollPeriod(YearMonth v) {
        payrollPeriod = v == null ? null : v.toString();
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company v) {
        company = v;
    }

    public String getBatchReference() {
        return batchReference;
    }

    public void setBatchReference(String v) {
        batchReference = v;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String v) {
        originalFilename = v;
    }

    public BigDecimal getExpectedTotal() {
        return expectedTotal;
    }

    public void setExpectedTotal(BigDecimal v) {
        expectedTotal = v;
    }

    public BigDecimal getReceivedTotal() {
        return receivedTotal;
    }

    public void setReceivedTotal(BigDecimal v) {
        receivedTotal = v;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int v) {
        recordCount = v;
    }

    public User getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(User v) {
        uploadedBy = v;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime v) {
        uploadedAt = v;
    }

    public LocalDateTime getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(LocalDateTime v) {
        postedAt = v;
    }

    public PayrollStatus getStatus() {
        return status;
    }

    public void setStatus(PayrollStatus v) {
        status = v;
    }
}
