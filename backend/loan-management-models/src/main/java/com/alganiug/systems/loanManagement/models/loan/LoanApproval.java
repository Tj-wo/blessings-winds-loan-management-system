package com.alganiug.systems.loanManagement.models.loan;

import com.alganiug.systems.loanManagement.models.base.BaseEntity;
import com.alganiug.systems.loanManagement.models.constants.*;
import com.alganiug.systems.loanManagement.models.security.User;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_approvals", indexes = @Index(name = "idx_approval_loan_stage", columnList = "loan_id,approval_stage"))
public class LoanApproval extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loan_id", nullable = false)

    private Loan loan;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_stage", nullable = false, length = 30)

    private ApprovalStage approvalStage;

    @Enumerated(EnumType.STRING)
    @Column(name = "decision", nullable = false, length = 40)

    private ApprovalDecision decision;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "decided_by", nullable = false)

    private User decidedBy;

    @Column(name = "offered_amount", precision = 19, scale = 2)

    private BigDecimal offeredAmount;

    @Column(name = "offered_term_months")

    private Integer offeredTermMonths;

    @Column(name = "comment", nullable = false, length = 1000)

    private String comment;

    @Column(name = "decided_at", nullable = false)

    private LocalDateTime decidedAt;

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan v) {
        loan = v;
    }

    public ApprovalStage getApprovalStage() {
        return approvalStage;
    }

    public void setApprovalStage(ApprovalStage v) {
        approvalStage = v;
    }

    public ApprovalDecision getDecision() {
        return decision;
    }

    public void setDecision(ApprovalDecision v) {
        decision = v;
    }

    public User getDecidedBy() {
        return decidedBy;
    }

    public void setDecidedBy(User v) {
        decidedBy = v;
    }

    public BigDecimal getOfferedAmount() {
        return offeredAmount;
    }

    public void setOfferedAmount(BigDecimal v) {
        offeredAmount = v;
    }

    public Integer getOfferedTermMonths() {
        return offeredTermMonths;
    }

    public void setOfferedTermMonths(Integer v) {
        offeredTermMonths = v;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String v) {
        comment = v;
    }

    public LocalDateTime getDecidedAt() {
        return decidedAt;
    }

    public void setDecidedAt(LocalDateTime v) {
        decidedAt = v;
    }
}
