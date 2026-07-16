package com.alganiug.systems.loanManagement.models.payroll;

import com.alganiug.systems.loanManagement.models.base.BaseEntity;
import com.alganiug.systems.loanManagement.models.employee.Employee;
import com.alganiug.systems.loanManagement.models.loan.Loan;
import com.alganiug.systems.loanManagement.models.loan.Repayment;
import com.alganiug.systems.loanManagement.models.constants.DeductionStatus;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "payroll_deductions", uniqueConstraints = @UniqueConstraint(name = "uk_batch_loan_deduction", columnNames = {
        "batch_id", "loan_id" }))
public class PayrollDeduction extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "batch_id", nullable = false)

    private PayrollDeductionBatch batch;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)

    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loan_id", nullable = false)

    private Loan loan;

    @Column(name = "expected_amount", nullable = false, precision = 19, scale = 2)

    private BigDecimal expectedAmount;

    @Column(name = "deducted_amount", nullable = false, precision = 19, scale = 2)

    private BigDecimal deductedAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)

    private DeductionStatus status = DeductionStatus.PENDING;

    @OneToOne(mappedBy = "payrollDeduction", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)

    private Repayment repayment;

    @Column(name = "reconciliation_comment", length = 500)

    private String reconciliationComment;

    public PayrollDeductionBatch getBatch() {
        return batch;
    }

    public void setBatch(PayrollDeductionBatch v) {
        batch = v;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee v) {
        employee = v;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan v) {
        loan = v;
    }

    public BigDecimal getExpectedAmount() {
        return expectedAmount;
    }

    public void setExpectedAmount(BigDecimal v) {
        expectedAmount = v;
    }

    public BigDecimal getDeductedAmount() {
        return deductedAmount;
    }

    public void setDeductedAmount(BigDecimal v) {
        deductedAmount = v;
    }

    public DeductionStatus getStatus() {
        return status;
    }

    public void setStatus(DeductionStatus v) {
        status = v;
    }

    public Repayment getRepayment() {
        return repayment;
    }

    public void setRepayment(Repayment repayment) {
        this.repayment = repayment;
        if (repayment != null && repayment.getPayrollDeduction() != this) {
            repayment.setPayrollDeduction(this);
        }
    }

    public String getReconciliationComment() {
        return reconciliationComment;
    }

    public void setReconciliationComment(String v) {
        reconciliationComment = v;
    }
}
