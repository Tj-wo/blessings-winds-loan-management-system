package com.alganiug.systems.loanManagement.models.security;

public final class PermissionConstants {

    private PermissionConstants() {
    }

    @SystemPermission(name = "Create Company", description = "Allows a user to create company records")
    public static final String PERM_COMPANY_CREATE = "COMPANY_CREATE";

    @SystemPermission(name = "View Company", description = "Allows a user to view company records")
    public static final String PERM_COMPANY_VIEW = "COMPANY_VIEW";

    @SystemPermission(name = "Edit Company", description = "Allows a user to edit company records")
    public static final String PERM_COMPANY_EDIT = "COMPANY_EDIT";

    @SystemPermission(name = "Delete Company", description = "Allows a user to delete company records")
    public static final String PERM_COMPANY_DELETE = "COMPANY_DELETE";

    @SystemPermission(name = "Create Employee", description = "Allows a user to create employee records")
    public static final String PERM_EMPLOYEE_CREATE = "EMPLOYEE_CREATE";

    @SystemPermission(name = "View Employee", description = "Allows a user to view employee records")
    public static final String PERM_EMPLOYEE_VIEW = "EMPLOYEE_VIEW";

    @SystemPermission(name = "Edit Employee", description = "Allows a user to edit employee records")
    public static final String PERM_EMPLOYEE_EDIT = "EMPLOYEE_EDIT";

    @SystemPermission(name = "Delete Employee", description = "Allows a user to delete employee records")
    public static final String PERM_EMPLOYEE_DELETE = "EMPLOYEE_DELETE";

    @SystemPermission(name = "Create Document", description = "Allows a user to create document records")
    public static final String PERM_DOCUMENT_CREATE = "DOCUMENT_CREATE";

    @SystemPermission(name = "View Document", description = "Allows a user to view document records")
    public static final String PERM_DOCUMENT_VIEW = "DOCUMENT_VIEW";

    @SystemPermission(name = "Edit Document", description = "Allows a user to edit document records")
    public static final String PERM_DOCUMENT_EDIT = "DOCUMENT_EDIT";

    @SystemPermission(name = "Delete Document", description = "Allows a user to delete document records")
    public static final String PERM_DOCUMENT_DELETE = "DOCUMENT_DELETE";

    @SystemPermission(name = "Create User", description = "Allows a user to create user records")
    public static final String PERM_USER_CREATE = "USER_CREATE";

    @SystemPermission(name = "View User", description = "Allows a user to view user records")
    public static final String PERM_USER_VIEW = "USER_VIEW";

    @SystemPermission(name = "Edit User", description = "Allows a user to edit user records")
    public static final String PERM_USER_EDIT = "USER_EDIT";

    @SystemPermission(name = "Delete User", description = "Allows a user to delete user records")
    public static final String PERM_USER_DELETE = "USER_DELETE";

    @SystemPermission(name = "Create Role", description = "Allows a user to create role records")
    public static final String PERM_ROLE_CREATE = "ROLE_CREATE";

    @SystemPermission(name = "View Role", description = "Allows a user to view role records")
    public static final String PERM_ROLE_VIEW = "ROLE_VIEW";

    @SystemPermission(name = "Edit Role", description = "Allows a user to edit role records")
    public static final String PERM_ROLE_EDIT = "ROLE_EDIT";

    @SystemPermission(name = "Delete Role", description = "Allows a user to delete role records")
    public static final String PERM_ROLE_DELETE = "ROLE_DELETE";

    @SystemPermission(name = "Create Permission", description = "Allows a user to create permission records")
    public static final String PERM_PERMISSION_CREATE = "PERMISSION_CREATE";

    @SystemPermission(name = "View Permission", description = "Allows a user to view permission records")
    public static final String PERM_PERMISSION_VIEW = "PERMISSION_VIEW";

    @SystemPermission(name = "Edit Permission", description = "Allows a user to edit permission records")
    public static final String PERM_PERMISSION_EDIT = "PERMISSION_EDIT";

    @SystemPermission(name = "Delete Permission", description = "Allows a user to delete permission records")
    public static final String PERM_PERMISSION_DELETE = "PERMISSION_DELETE";

    @SystemPermission(name = "Create Loan product", description = "Allows a user to create loan product records")
    public static final String PERM_LOAN_PRODUCT_CREATE = "LOAN_PRODUCT_CREATE";

    @SystemPermission(name = "View Loan product", description = "Allows a user to view loan product records")
    public static final String PERM_LOAN_PRODUCT_VIEW = "LOAN_PRODUCT_VIEW";

    @SystemPermission(name = "Edit Loan product", description = "Allows a user to edit loan product records")
    public static final String PERM_LOAN_PRODUCT_EDIT = "LOAN_PRODUCT_EDIT";

    @SystemPermission(name = "Delete Loan product", description = "Allows a user to delete loan product records")
    public static final String PERM_LOAN_PRODUCT_DELETE = "LOAN_PRODUCT_DELETE";

    @SystemPermission(name = "Create Loan", description = "Allows a user to create loan records")
    public static final String PERM_LOAN_CREATE = "LOAN_CREATE";

    @SystemPermission(name = "View Loan", description = "Allows a user to view loan records")
    public static final String PERM_LOAN_VIEW = "LOAN_VIEW";

    @SystemPermission(name = "Edit Loan", description = "Allows a user to edit loan records")
    public static final String PERM_LOAN_EDIT = "LOAN_EDIT";

    @SystemPermission(name = "Delete Loan", description = "Allows a user to delete loan records")
    public static final String PERM_LOAN_DELETE = "LOAN_DELETE";

    @SystemPermission(name = "Create Loan approval", description = "Allows a user to create loan approval records")
    public static final String PERM_LOAN_APPROVAL_CREATE = "LOAN_APPROVAL_CREATE";

    @SystemPermission(name = "View Loan approval", description = "Allows a user to view loan approval records")
    public static final String PERM_LOAN_APPROVAL_VIEW = "LOAN_APPROVAL_VIEW";

    @SystemPermission(name = "Edit Loan approval", description = "Allows a user to edit loan approval records")
    public static final String PERM_LOAN_APPROVAL_EDIT = "LOAN_APPROVAL_EDIT";

    @SystemPermission(name = "Delete Loan approval", description = "Allows a user to delete loan approval records")
    public static final String PERM_LOAN_APPROVAL_DELETE = "LOAN_APPROVAL_DELETE";

    @SystemPermission(name = "Create Disbursement", description = "Allows a user to create disbursement records")
    public static final String PERM_DISBURSEMENT_CREATE = "DISBURSEMENT_CREATE";

    @SystemPermission(name = "View Disbursement", description = "Allows a user to view disbursement records")
    public static final String PERM_DISBURSEMENT_VIEW = "DISBURSEMENT_VIEW";

    @SystemPermission(name = "Edit Disbursement", description = "Allows a user to edit disbursement records")
    public static final String PERM_DISBURSEMENT_EDIT = "DISBURSEMENT_EDIT";

    @SystemPermission(name = "Delete Disbursement", description = "Allows a user to delete disbursement records")
    public static final String PERM_DISBURSEMENT_DELETE = "DISBURSEMENT_DELETE";

    @SystemPermission(name = "Create Repayment schedule", description = "Allows a user to create repayment schedule records")
    public static final String PERM_REPAYMENT_SCHEDULE_CREATE = "REPAYMENT_SCHEDULE_CREATE";

    @SystemPermission(name = "View Repayment schedule", description = "Allows a user to view repayment schedule records")
    public static final String PERM_REPAYMENT_SCHEDULE_VIEW = "REPAYMENT_SCHEDULE_VIEW";

    @SystemPermission(name = "Edit Repayment schedule", description = "Allows a user to edit repayment schedule records")
    public static final String PERM_REPAYMENT_SCHEDULE_EDIT = "REPAYMENT_SCHEDULE_EDIT";

    @SystemPermission(name = "Delete Repayment schedule", description = "Allows a user to delete repayment schedule records")
    public static final String PERM_REPAYMENT_SCHEDULE_DELETE = "REPAYMENT_SCHEDULE_DELETE";

    @SystemPermission(name = "Create Repayment", description = "Allows a user to create repayment records")
    public static final String PERM_REPAYMENT_CREATE = "REPAYMENT_CREATE";

    @SystemPermission(name = "View Repayment", description = "Allows a user to view repayment records")
    public static final String PERM_REPAYMENT_VIEW = "REPAYMENT_VIEW";

    @SystemPermission(name = "Edit Repayment", description = "Allows a user to edit repayment records")
    public static final String PERM_REPAYMENT_EDIT = "REPAYMENT_EDIT";

    @SystemPermission(name = "Delete Repayment", description = "Allows a user to delete repayment records")
    public static final String PERM_REPAYMENT_DELETE = "REPAYMENT_DELETE";

    @SystemPermission(name = "Create Penalty", description = "Allows a user to create penalty records")
    public static final String PERM_PENALTY_CREATE = "PENALTY_CREATE";

    @SystemPermission(name = "View Penalty", description = "Allows a user to view penalty records")
    public static final String PERM_PENALTY_VIEW = "PENALTY_VIEW";

    @SystemPermission(name = "Edit Penalty", description = "Allows a user to edit penalty records")
    public static final String PERM_PENALTY_EDIT = "PENALTY_EDIT";

    @SystemPermission(name = "Delete Penalty", description = "Allows a user to delete penalty records")
    public static final String PERM_PENALTY_DELETE = "PENALTY_DELETE";

    @SystemPermission(name = "Create Payroll batch", description = "Allows a user to create payroll batch records")
    public static final String PERM_PAYROLL_BATCH_CREATE = "PAYROLL_BATCH_CREATE";

    @SystemPermission(name = "View Payroll batch", description = "Allows a user to view payroll batch records")
    public static final String PERM_PAYROLL_BATCH_VIEW = "PAYROLL_BATCH_VIEW";

    @SystemPermission(name = "Edit Payroll batch", description = "Allows a user to edit payroll batch records")
    public static final String PERM_PAYROLL_BATCH_EDIT = "PAYROLL_BATCH_EDIT";

    @SystemPermission(name = "Delete Payroll batch", description = "Allows a user to delete payroll batch records")
    public static final String PERM_PAYROLL_BATCH_DELETE = "PAYROLL_BATCH_DELETE";

    @SystemPermission(name = "Create Payroll deduction", description = "Allows a user to create payroll deduction records")
    public static final String PERM_PAYROLL_DEDUCTION_CREATE = "PAYROLL_DEDUCTION_CREATE";

    @SystemPermission(name = "View Payroll deduction", description = "Allows a user to view payroll deduction records")
    public static final String PERM_PAYROLL_DEDUCTION_VIEW = "PAYROLL_DEDUCTION_VIEW";

    @SystemPermission(name = "Edit Payroll deduction", description = "Allows a user to edit payroll deduction records")
    public static final String PERM_PAYROLL_DEDUCTION_EDIT = "PAYROLL_DEDUCTION_EDIT";

    @SystemPermission(name = "Delete Payroll deduction", description = "Allows a user to delete payroll deduction records")
    public static final String PERM_PAYROLL_DEDUCTION_DELETE = "PAYROLL_DEDUCTION_DELETE";

    @SystemPermission(name = "Create Notification", description = "Allows a user to create notification records")
    public static final String PERM_NOTIFICATION_CREATE = "NOTIFICATION_CREATE";

    @SystemPermission(name = "View Notification", description = "Allows a user to view notification records")
    public static final String PERM_NOTIFICATION_VIEW = "NOTIFICATION_VIEW";

    @SystemPermission(name = "Edit Notification", description = "Allows a user to edit notification records")
    public static final String PERM_NOTIFICATION_EDIT = "NOTIFICATION_EDIT";

    @SystemPermission(name = "Delete Notification", description = "Allows a user to delete notification records")
    public static final String PERM_NOTIFICATION_DELETE = "NOTIFICATION_DELETE";

    @SystemPermission(name = "Create Audit log", description = "Allows a user to create audit log records")
    public static final String PERM_AUDIT_LOG_CREATE = "AUDIT_LOG_CREATE";

    @SystemPermission(name = "View Audit log", description = "Allows a user to view audit log records")
    public static final String PERM_AUDIT_LOG_VIEW = "AUDIT_LOG_VIEW";

    @SystemPermission(name = "Edit Audit log", description = "Allows a user to edit audit log records")
    public static final String PERM_AUDIT_LOG_EDIT = "AUDIT_LOG_EDIT";

    @SystemPermission(name = "Delete Audit log", description = "Allows a user to delete audit log records")
    public static final String PERM_AUDIT_LOG_DELETE = "AUDIT_LOG_DELETE";

    @SystemPermission(name = "Create System setting", description = "Allows a user to create system setting records")
    public static final String PERM_SYSTEM_SETTING_CREATE = "SYSTEM_SETTING_CREATE";

    @SystemPermission(name = "View System setting", description = "Allows a user to view system setting records")
    public static final String PERM_SYSTEM_SETTING_VIEW = "SYSTEM_SETTING_VIEW";

    @SystemPermission(name = "Edit System setting", description = "Allows a user to edit system setting records")
    public static final String PERM_SYSTEM_SETTING_EDIT = "SYSTEM_SETTING_EDIT";

    @SystemPermission(name = "Delete System setting", description = "Allows a user to delete system setting records")
    public static final String PERM_SYSTEM_SETTING_DELETE = "SYSTEM_SETTING_DELETE";

    @SystemPermission(name = "Approve loan", description = "Allows a user to approve loan applications")
    public static final String PERM_LOAN_APPROVE = "LOAN_APPROVE";

    @SystemPermission(name = "Reject loan", description = "Allows a user to reject loan applications")
    public static final String PERM_LOAN_REJECT = "LOAN_REJECT";

    @SystemPermission(name = "Disburse loan", description = "Allows a user to disburse approved loans")
    public static final String PERM_LOAN_DISBURSE = "LOAN_DISBURSE";

    @SystemPermission(name = "Verify document", description = "Allows a user to verify KYC documents")
    public static final String PERM_DOCUMENT_VERIFY = "DOCUMENT_VERIFY";

    @SystemPermission(name = "Post payroll batch", description = "Allows a user to post payroll deduction batches")
    public static final String PERM_PAYROLL_BATCH_POST = "PAYROLL_BATCH_POST";

    @SystemPermission(name = "Waive penalty", description = "Allows a user to waive assessed penalties")
    public static final String PERM_PENALTY_WAIVE = "PENALTY_WAIVE";

    @SystemPermission(name = "Export report", description = "Allows a user to export system reports")
    public static final String PERM_REPORT_EXPORT = "REPORT_EXPORT";
}
