package com.alganiug.systems.loanManagement.models.loan;

import com.alganiug.systems.loanManagement.models.base.BaseEntity;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "loan_products", uniqueConstraints = @UniqueConstraint(name = "uk_loan_product_name", columnNames = "name"))
public class LoanProduct extends BaseEntity {
    @Column(name = "name", nullable = false, length = 100)

    private String name;

    @Column(name = "monthly_interest_rate", nullable = false, precision = 7, scale = 4)

    private BigDecimal monthlyInterestRate;

    @Column(name = "minimum_amount", nullable = false, precision = 19, scale = 2)

    private BigDecimal minimumAmount;

    @Column(name = "maximum_amount", nullable = false, precision = 19, scale = 2)

    private BigDecimal maximumAmount;

    @Column(name = "minimum_term_months", nullable = false)

    private int minimumTermMonths;

    @Column(name = "maximum_term_months", nullable = false)

    private int maximumTermMonths;

    @Column(name = "maximum_salary_deduction_percent", nullable = false, precision = 7, scale = 4)

    private BigDecimal maximumSalaryDeductionPercent;

    @Column(name = "grace_period_days", nullable = false)

    private int gracePeriodDays;

    @Column(name = "default_threshold_days", nullable = false)

    private int defaultThresholdDays;

    @Column(name = "penalty_rate", nullable = false, precision = 7, scale = 4)

    private BigDecimal penaltyRate = BigDecimal.ZERO;

    public String getName() {
        return name;
    }

    public void setName(String v) {
        name = v;
    }

    public BigDecimal getMonthlyInterestRate() {
        return monthlyInterestRate;
    }

    public void setMonthlyInterestRate(BigDecimal v) {
        monthlyInterestRate = v;
    }

    public BigDecimal getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(BigDecimal v) {
        minimumAmount = v;
    }

    public BigDecimal getMaximumAmount() {
        return maximumAmount;
    }

    public void setMaximumAmount(BigDecimal v) {
        maximumAmount = v;
    }

    public int getMinimumTermMonths() {
        return minimumTermMonths;
    }

    public void setMinimumTermMonths(int v) {
        minimumTermMonths = v;
    }

    public int getMaximumTermMonths() {
        return maximumTermMonths;
    }

    public void setMaximumTermMonths(int v) {
        maximumTermMonths = v;
    }

    public BigDecimal getMaximumSalaryDeductionPercent() {
        return maximumSalaryDeductionPercent;
    }

    public void setMaximumSalaryDeductionPercent(BigDecimal v) {
        maximumSalaryDeductionPercent = v;
    }

    public int getGracePeriodDays() {
        return gracePeriodDays;
    }

    public void setGracePeriodDays(int v) {
        gracePeriodDays = v;
    }

    public int getDefaultThresholdDays() {
        return defaultThresholdDays;
    }

    public void setDefaultThresholdDays(int v) {
        defaultThresholdDays = v;
    }

    public BigDecimal getPenaltyRate() {
        return penaltyRate;
    }

    public void setPenaltyRate(BigDecimal v) {
        penaltyRate = v;
    }
}
