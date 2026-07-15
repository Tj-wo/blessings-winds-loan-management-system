package com.alganiug.systems.loanManagement.models.loan;

import com.alganiug.systems.loanManagement.models.base.BaseEntity;
import com.alganiug.systems.loanManagement.models.constants.ScheduleStatus;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "repayment_schedule_items", uniqueConstraints = @UniqueConstraint(name = "uk_schedule_installment", columnNames = {
        "loan_id",
        "installment_number" }), indexes = @Index(name = "idx_schedule_due_status", columnList = "due_date,status"))
public class RepaymentScheduleItem extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loan_id", nullable = false)

    private Loan loan;

    @Column(name = "installment_number", nullable = false)

    private int installmentNumber;

    @Column(name = "due_date", nullable = false)

    private LocalDate dueDate;

    @Column(name = "expected_amount", nullable = false, precision = 19, scale = 2)

    private BigDecimal expectedAmount;

    @Column(name = "paid_amount", nullable = false, precision = 19, scale = 2)

    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(name = "remaining_loan_balance", nullable = false, precision = 19, scale = 2)

    private BigDecimal remainingLoanBalance;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)

    private ScheduleStatus status = ScheduleStatus.PENDING;

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan v) {
        loan = v;
    }

    public int getInstallmentNumber() {
        return installmentNumber;
    }

    public void setInstallmentNumber(int v) {
        installmentNumber = v;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate v) {
        dueDate = v;
    }

    public BigDecimal getExpectedAmount() {
        return expectedAmount;
    }

    public void setExpectedAmount(BigDecimal v) {
        expectedAmount = v;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal v) {
        paidAmount = v;
    }

    public BigDecimal getRemainingLoanBalance() {
        return remainingLoanBalance;
    }

    public void setRemainingLoanBalance(BigDecimal v) {
        remainingLoanBalance = v;
    }

    public ScheduleStatus getStatus() {
        return status;
    }

    public void setStatus(ScheduleStatus v) {
        status = v;
    }
}
