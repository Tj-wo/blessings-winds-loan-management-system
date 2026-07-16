package com.alganiug.systems.loanManagement.models.loan;

import com.alganiug.systems.loanManagement.models.base.BaseEntity;
import com.alganiug.systems.loanManagement.models.constants.PenaltyStatus;
import com.alganiug.systems.loanManagement.models.security.User;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "penalty_charges", indexes = @Index(name = "idx_penalty_loan_status", columnList = "loan_id,status"))
public class PenaltyCharge extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loan_id", nullable = false)

    private Loan loan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_item_id")

    private RepaymentScheduleItem scheduleItem;

    @Column(name = "reason", nullable = false, length = 255)

    private String reason;

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)

    private PenaltyStatus status = PenaltyStatus.ASSESSED;

    @Column(name = "assessed_on", nullable = false)

    private LocalDate assessedOn;

    @Column(name = "waived_on")

    private LocalDate waivedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waived_by")

    private User waivedBy;

    @Column(name = "waiver_reason", length = 500)

    private String waiverReason;

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan v) {
        loan = v;
    }

    public RepaymentScheduleItem getScheduleItem() {
        return scheduleItem;
    }

    public void setScheduleItem(RepaymentScheduleItem v) {
        scheduleItem = v;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String v) {
        reason = v;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal v) {
        amount = v;
    }

    public PenaltyStatus getStatus() {
        return status;
    }

    public void setStatus(PenaltyStatus v) {
        status = v;
    }

    public LocalDate getAssessedOn() {
        return assessedOn;
    }

    public void setAssessedOn(LocalDate v) {
        assessedOn = v;
    }

    public LocalDate getWaivedOn() {
        return waivedOn;
    }

    public void setWaivedOn(LocalDate v) {
        waivedOn = v;
    }

    public User getWaivedBy() {
        return waivedBy;
    }

    public void setWaivedBy(User v) {
        waivedBy = v;
    }

    public String getWaiverReason() {
        return waiverReason;
    }

    public void setWaiverReason(String v) {
        waiverReason = v;
    }
}
