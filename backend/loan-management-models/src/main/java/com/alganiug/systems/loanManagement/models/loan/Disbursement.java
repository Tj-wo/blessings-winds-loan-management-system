package com.alganiug.systems.loanManagement.models.loan;

import com.alganiug.systems.loanManagement.models.base.BaseEntity;
import com.alganiug.systems.loanManagement.models.constants.*;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "disbursements", uniqueConstraints = {
        @UniqueConstraint(name = "uk_disbursement_loan", columnNames = "loan_id"),
        @UniqueConstraint(name = "uk_disbursement_reference", columnNames = "transaction_reference") })
public class Disbursement extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loan_id", nullable = false)

    private Loan loan;

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "method", nullable = false, length = 30)

    private DisbursementMethod method;

    @Column(name = "destination", nullable = false, length = 150)

    private String destination;

    @Column(name = "transaction_reference", nullable = false, length = 80)

    private String transactionReference;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)

    private DisbursementStatus status = DisbursementStatus.PENDING;

    @Column(name = "disbursed_on")

    private LocalDate disbursedOn;

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

    public DisbursementMethod getMethod() {
        return method;
    }

    public void setMethod(DisbursementMethod v) {
        method = v;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String v) {
        destination = v;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String v) {
        transactionReference = v;
    }

    public DisbursementStatus getStatus() {
        return status;
    }

    public void setStatus(DisbursementStatus v) {
        status = v;
    }

    public LocalDate getDisbursedOn() {
        return disbursedOn;
    }

    public void setDisbursedOn(LocalDate v) {
        disbursedOn = v;
    }
}
