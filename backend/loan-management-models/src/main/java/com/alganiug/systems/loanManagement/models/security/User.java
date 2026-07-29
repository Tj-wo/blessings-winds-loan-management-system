package com.alganiug.systems.loanManagement.models.security;

import com.alganiug.systems.loanManagement.models.base.BaseEntity;
import com.alganiug.systems.loanManagement.models.company.Company;
import com.alganiug.systems.loanManagement.models.employee.Employee;
import com.alganiug.systems.loanManagement.models.constants.AccountStatus;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(name = "uk_user_username", columnNames = "username"))
public class User extends BaseEntity {
    @Column(name = "username", nullable = false, length = 100)

    private String username;

    @Column(name = "password_hash", nullable = false, length = 255)

    private String passwordHash;

    @Column(name = "display_name", nullable = false, length = 150)

    private String displayName;

    @Column(name = "email", length = 150)

    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false, length = 20)

    private AccountStatus accountStatus = AccountStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")

    private Company company;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", unique = true)

    private Employee employee;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"), uniqueConstraints = @UniqueConstraint(name = "uk_user_role", columnNames = {
            "user_id", "role_id" }))

    private Set<Role> roles = new LinkedHashSet<>();

    @Column(name = "last_login_at")

    private LocalDateTime lastLoginAt;

    @Column(name = "password_reset_otp_hash", length = 255)
    private String passwordResetOtpHash;

    @Column(name = "password_reset_otp_expires_at")
    private LocalDateTime passwordResetOtpExpiresAt;

    @Column(name = "password_reset_otp_attempts", nullable = false)
    private int passwordResetOtpAttempts;

    public String getUsername() {
        return username;
    }

    public void setUsername(String v) {
        username = v;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String v) {
        passwordHash = v;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String v) {
        displayName = v;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String v) {
        email = v;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus v) {
        accountStatus = v;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company v) {
        company = v;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee v) {
        employee = v;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime v) {
        lastLoginAt = v;
    }

    public String getPasswordResetOtpHash() { return passwordResetOtpHash; }
    public void setPasswordResetOtpHash(String value) { passwordResetOtpHash = value; }
    public LocalDateTime getPasswordResetOtpExpiresAt() { return passwordResetOtpExpiresAt; }
    public void setPasswordResetOtpExpiresAt(LocalDateTime value) { passwordResetOtpExpiresAt = value; }
    public int getPasswordResetOtpAttempts() { return passwordResetOtpAttempts; }
    public void setPasswordResetOtpAttempts(int value) { passwordResetOtpAttempts = value; }
}
