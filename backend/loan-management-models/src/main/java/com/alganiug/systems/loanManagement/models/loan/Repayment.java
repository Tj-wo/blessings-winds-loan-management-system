package com.alganiug.systems.loanManagement.models.loan;

import com.alganiug.systems.loanManagement.models.base.BaseEntity;
import com.alganiug.systems.loanManagement.models.constants.RepaymentStatus;
import com.alganiug.systems.loanManagement.models.payroll.PayrollDeduction;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "repayments", uniqueConstraints = @UniqueConstraint(name = "uk_repayment_reference", columnNames = "payment_reference"), indexes = @Index(name = "idx_repayment_loan_date", columnList = "loan_id,payment_date"))
public class Repayment extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payroll_deduction_id", nullable = false, unique = true)

    private PayrollDeduction payrollDeduction;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loan_id", nullable = false)

    private Loan loan;

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)

    private BigDecimal amount;

    @Column(name = "payment_date", nullable = false)

    private LocalDate paymentDate;

    @Column(name = "payment_reference", nullable = false, length = 80)

    private String paymentReference;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)

    private RepaymentStatus status = RepaymentStatus.PENDING;

    public PayrollDeduction getPayrollDeduction() {
        return payrollDeduction;
    }

    public void setPayrollDeduction(PayrollDeduction payrollDeduction) {
        this.payrollDeduction = payrollDeduction;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan v) {
        loan = v;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal v) {
        amount = v;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate v) {
        paymentDate = v;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String v) {
        paymentReference = v;
    }

    public RepaymentStatus getStatus() {
        return status;
    }

    public void setStatus(RepaymentStatus v) {
        status = v;
    }
}
