package com.alganiug.systems.loanManagement.models.employee;

import com.alganiug.systems.loanManagement.models.base.BaseEntity;
import com.alganiug.systems.loanManagement.models.company.Company;
import com.alganiug.systems.loanManagement.models.constants.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "employees", uniqueConstraints = {
        @UniqueConstraint(name = "uk_employee_company_number", columnNames = { "company_id", "employee_number" }),
        @UniqueConstraint(name = "uk_employee_national_id", columnNames = "national_id"),
        @UniqueConstraint(name = "uk_employee_payroll", columnNames = { "company_id", "payroll_number" }) })
public class Employee extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)

    private Company company;

    @Column(name = "employee_number", nullable = false, length = 50)

    private String employeeNumber;

    @Column(name = "payroll_number", nullable = false, length = 50)

    private String payrollNumber;

    @Column(name = "first_name", nullable = false, length = 80)

    private String firstName;

    @Column(name = "last_name", nullable = false, length = 80)

    private String lastName;

    @Column(name = "national_id", nullable = false, length = 50)

    private String nationalId;

    @Column(name = "date_of_birth")

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 20)

    private Gender gender;

    @Column(name = "phone", nullable = false, length = 30)

    private String phone;

    @Column(name = "email", length = 150)

    private String email;

    @Column(name = "physical_address", length = 255)

    private String physicalAddress;

    @Column(name = "next_of_kin_name", length = 150)

    private String nextOfKinName;

    @Column(name = "next_of_kin_phone", length = 30)

    private String nextOfKinPhone;

    @Column(name = "department", length = 100)

    private String department;

    @Column(name = "position_title", length = 100)

    private String positionTitle;

    @Column(name = "employment_date")

    private LocalDate employmentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_status", nullable = false, length = 30)

    private EmploymentStatus employmentStatus;

    @Column(name = "monthly_salary", nullable = false, precision = 19, scale = 2)

    private BigDecimal monthlySalary = BigDecimal.ZERO;

    @Column(name = "bank_name", length = 100)

    private String bankName;

    @Column(name = "bank_account_number", length = 80)

    private String bankAccountNumber;

    @Column(name = "bank_branch", length = 100)

    private String bankBranch;

    @Column(name = "mobile_money_number", length = 30)

    private String mobileMoneyNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "kyc_status", nullable = false, length = 40)

    private KycStatus kycStatus = KycStatus.PENDING_VERIFICATION;

    @Column(name = "risk_score", nullable = false)

    private int riskScore;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company v) {
        company = v;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String v) {
        employeeNumber = v;
    }

    public String getPayrollNumber() {
        return payrollNumber;
    }

    public void setPayrollNumber(String v) {
        payrollNumber = v;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String v) {
        firstName = v;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String v) {
        lastName = v;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String v) {
        nationalId = v;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate v) {
        dateOfBirth = v;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender v) {
        gender = v;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String v) {
        phone = v;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String v) {
        email = v;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(String v) {
        physicalAddress = v;
    }

    public String getNextOfKinName() {
        return nextOfKinName;
    }

    public void setNextOfKinName(String v) {
        nextOfKinName = v;
    }

    public String getNextOfKinPhone() {
        return nextOfKinPhone;
    }

    public void setNextOfKinPhone(String v) {
        nextOfKinPhone = v;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String v) {
        department = v;
    }

    public String getPositionTitle() {
        return positionTitle;
    }

    public void setPositionTitle(String v) {
        positionTitle = v;
    }

    public LocalDate getEmploymentDate() {
        return employmentDate;
    }

    public void setEmploymentDate(LocalDate v) {
        employmentDate = v;
    }

    public EmploymentStatus getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(EmploymentStatus v) {
        employmentStatus = v;
    }

    public BigDecimal getMonthlySalary() {
        return monthlySalary;
    }

    public void setMonthlySalary(BigDecimal v) {
        monthlySalary = v;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String v) {
        bankName = v;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String v) {
        bankAccountNumber = v;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String v) {
        bankBranch = v;
    }

    public String getMobileMoneyNumber() {
        return mobileMoneyNumber;
    }

    public void setMobileMoneyNumber(String v) {
        mobileMoneyNumber = v;
    }

    public KycStatus getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(KycStatus v) {
        kycStatus = v;
    }

    public int getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(int v) {
        riskScore = v;
    }
}
