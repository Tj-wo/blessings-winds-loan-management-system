package com.alganiug.systems.loanManagement.models.security;

public final class RoleConstants {

    private RoleConstants() {
    }

    @SystemRole(name = "Administrator", description = "Full system administration access")
    public static final String ROLE_ADMINISTRATOR = "Administrator";

    @SystemRole(name = "Loan manager", description = "Loan assessment, approval, disbursement and repayment management")
    public static final String ROLE_LOAN_MANAGER = "Loan Manager";

    @SystemRole(name = "HR supervisor", description = "Company employee, KYC and payroll management")
    public static final String ROLE_HR_SUPERVISOR = "HR Supervisor";

    @SystemRole(name = "Employee", description = "Employee self-service loan access")
    public static final String ROLE_EMPLOYEE = "Employee";
}
