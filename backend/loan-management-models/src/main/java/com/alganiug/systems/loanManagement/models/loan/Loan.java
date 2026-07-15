package com.alganiug.systems.loanManagement.models.loan;

import com.alganiug.systems.loanManagement.models.base.BaseEntity;
import com.alganiug.systems.loanManagement.models.company.Company;
import com.alganiug.systems.loanManagement.models.employee.Employee;
import com.alganiug.systems.loanManagement.models.constants.*;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "loans", uniqueConstraints = @UniqueConstraint(name = "uk_loan_reference", columnNames = "loan_reference"), indexes = {
        @Index(name = "idx_loan_company_status", columnList = "company_id,status"),
        @Index(name = "idx_loan_employee", columnList = "employee_id") })
public class Loan extends BaseEntity {
    @Column(name = "loan_reference", nullable = false, length = 40)

    private String loanReference;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)

    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)

    private Company company;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)

    private LoanProduct product;

    @Column(name = "requested_amount", nullable = false, precision = 19, scale = 2)

    private BigDecimal requestedAmount;

    @Column(name = "approved_amount", precision = 19, scale = 2)

    private BigDecimal approvedAmount;

    @Column(name = "purpose", nullable = false, length = 255)

    private String purpose;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_disbursement_method", nullable = false, length = 30)

    private DisbursementMethod preferredDisbursementMethod;

    @Column(name = "requested_term_months", nullable = false)

    private int requestedTermMonths;

    @Column(name = "approved_term_months")

    private Integer approvedTermMonths;

    @Column(name = "monthly_interest_rate", nullable = false, precision = 7, scale = 4)

    private BigDecimal monthlyInterestRate;

    @Column(name = "interest_amount", nullable = false, precision = 19, scale = 2)

    private BigDecimal interestAmount = BigDecimal.ZERO;

    @Column(name = "installment_amount", nullable = false, precision = 19, scale = 2)

    private BigDecimal installmentAmount = BigDecimal.ZERO;

    @Column(name = "total_payable", nullable = false, precision = 19, scale = 2)

    private BigDecimal totalPayable = BigDecimal.ZERO;

    @Column(name = "amount_paid", nullable = false, precision = 19, scale = 2)

    private BigDecimal amountPaid = BigDecimal.ZERO;

    @Column(name = "outstanding_balance", nullable = false, precision = 19, scale = 2)

    private BigDecimal outstandingBalance = BigDecimal.ZERO;

    @Column(name = "risk_score", nullable = false)

    private int riskScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)

    private LoanStatus status = LoanStatus.DRAFT;

    @Column(name = "requested_on")

    private LocalDate requestedOn;

    @Column(name = "approved_on")

    private LocalDate approvedOn;

    @Column(name = "disbursed_on")

    private LocalDate disbursedOn;

    @Column(name = "completed_on")

    private LocalDate completedOn;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("decidedAt ASC")

    private List<LoanApproval> approvals = new ArrayList<>();

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("installmentNumber ASC")

    private List<RepaymentScheduleItem> scheduleItems = new ArrayList<>();

    public void addApproval(LoanApproval v) {
        approvals.add(v);
        v.setLoan(this);
    }

    public void addScheduleItem(RepaymentScheduleItem v) {
        scheduleItems.add(v);
        v.setLoan(this);
    }

    public String getLoanReference() {
        return loanReference;
    }

    public void setLoanReference(String v) {
        loanReference = v;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee v) {
        employee = v;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company v) {
        company = v;
    }

    public LoanProduct getProduct() {
        return product;
    }

    public void setProduct(LoanProduct v) {
        product = v;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(BigDecimal v) {
        requestedAmount = v;
    }

    public BigDecimal getApprovedAmount() {
        return approvedAmount;
    }

    public void setApprovedAmount(BigDecimal v) {
        approvedAmount = v;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String v) {
        purpose = v;
    }

    public DisbursementMethod getPreferredDisbursementMethod() {
        return preferredDisbursementMethod;
    }

    public void setPreferredDisbursementMethod(DisbursementMethod v) {
        preferredDisbursementMethod = v;
    }

    public int getRequestedTermMonths() {
        return requestedTermMonths;
    }

    public void setRequestedTermMonths(int v) {
        requestedTermMonths = v;
    }

    public Integer getApprovedTermMonths() {
        return approvedTermMonths;
    }

    public void setApprovedTermMonths(Integer v) {
        approvedTermMonths = v;
    }

    public BigDecimal getMonthlyInterestRate() {
        return monthlyInterestRate;
    }

    public void setMonthlyInterestRate(BigDecimal v) {
        monthlyInterestRate = v;
    }

    public BigDecimal getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(BigDecimal v) {
        interestAmount = v;
    }

    public BigDecimal getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(BigDecimal v) {
        installmentAmount = v;
    }

    public BigDecimal getTotalPayable() {
        return totalPayable;
    }

    public void setTotalPayable(BigDecimal v) {
        totalPayable = v;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal v) {
        amountPaid = v;
    }

    public BigDecimal getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(BigDecimal v) {
        outstandingBalance = v;
    }

    public int getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(int v) {
        riskScore = v;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus v) {
        status = v;
    }

    public LocalDate getRequestedOn() {
        return requestedOn;
    }

    public void setRequestedOn(LocalDate v) {
        requestedOn = v;
    }

    public LocalDate getApprovedOn() {
        return approvedOn;
    }

    public void setApprovedOn(LocalDate v) {
        approvedOn = v;
    }

    public LocalDate getDisbursedOn() {
        return disbursedOn;
    }

    public void setDisbursedOn(LocalDate v) {
        disbursedOn = v;
    }

    public LocalDate getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(LocalDate v) {
        completedOn = v;
    }

    public List<LoanApproval> getApprovals() {
        return approvals;
    }

    public List<RepaymentScheduleItem> getScheduleItems() {
        return scheduleItems;
    }
}
