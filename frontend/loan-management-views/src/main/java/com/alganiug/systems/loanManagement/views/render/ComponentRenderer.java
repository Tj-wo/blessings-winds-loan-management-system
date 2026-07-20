package com.alganiug.systems.loanManagement.views.render;

import com.alganiug.systems.loanManagement.core.services.security.UserService;
import com.alganiug.systems.loanManagement.models.security.PermissionConstants;
import com.alganiug.systems.loanManagement.models.security.RoleConstants;
import com.alganiug.systems.loanManagement.models.security.User;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

@ManagedBean(name = "componentRenderer")
@SessionScoped
public class ComponentRenderer implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{userServiceImpl}")
    private UserService userService;

    private User loggedInUser;
    private boolean administrator;
    private Set<String> permissionCodes = Collections.emptySet();

    private String saveButtonStyle = "ui-button-info";
    private String cancelButtonStyle = "ui-button-outlined ui-button-help";
    private String backButtonStyle = "ui-button-warning";
    private String saveButtonIcon = "pi pi-check";
    private String cancelButtonIcon = "pi pi-times";
    private String backButtonIcon = "pi pi-backward";
    private boolean hasCreateCompanyPerm;
    private boolean hasViewCompanyPerm;
    private boolean hasEditCompanyPerm;
    private boolean hasDeleteCompanyPerm;
    private boolean hasCreateEmployeePerm;
    private boolean hasViewEmployeePerm;
    private boolean hasEditEmployeePerm;
    private boolean hasDeleteEmployeePerm;
    private boolean hasCreateDocumentPerm;
    private boolean hasViewDocumentPerm;
    private boolean hasEditDocumentPerm;
    private boolean hasDeleteDocumentPerm;
    private boolean hasCreateUserPerm;
    private boolean hasViewUserPerm;
    private boolean hasEditUserPerm;
    private boolean hasDeleteUserPerm;
    private boolean hasCreateRolePerm;
    private boolean hasViewRolePerm;
    private boolean hasEditRolePerm;
    private boolean hasDeleteRolePerm;
    private boolean hasCreatePermissionPerm;
    private boolean hasViewPermissionPerm;
    private boolean hasEditPermissionPerm;
    private boolean hasDeletePermissionPerm;
    private boolean hasCreateLoanProductPerm;
    private boolean hasViewLoanProductPerm;
    private boolean hasEditLoanProductPerm;
    private boolean hasDeleteLoanProductPerm;
    private boolean hasCreateLoanPerm;
    private boolean hasViewLoanPerm;
    private boolean hasEditLoanPerm;
    private boolean hasDeleteLoanPerm;
    private boolean hasCreateLoanApprovalPerm;
    private boolean hasViewLoanApprovalPerm;
    private boolean hasEditLoanApprovalPerm;
    private boolean hasDeleteLoanApprovalPerm;
    private boolean hasCreateDisbursementPerm;
    private boolean hasViewDisbursementPerm;
    private boolean hasEditDisbursementPerm;
    private boolean hasDeleteDisbursementPerm;
    private boolean hasCreateRepaymentSchedulePerm;
    private boolean hasViewRepaymentSchedulePerm;
    private boolean hasEditRepaymentSchedulePerm;
    private boolean hasDeleteRepaymentSchedulePerm;
    private boolean hasCreateRepaymentPerm;
    private boolean hasViewRepaymentPerm;
    private boolean hasEditRepaymentPerm;
    private boolean hasDeleteRepaymentPerm;
    private boolean hasCreatePenaltyPerm;
    private boolean hasViewPenaltyPerm;
    private boolean hasEditPenaltyPerm;
    private boolean hasDeletePenaltyPerm;
    private boolean hasCreatePayrollBatchPerm;
    private boolean hasViewPayrollBatchPerm;
    private boolean hasEditPayrollBatchPerm;
    private boolean hasDeletePayrollBatchPerm;
    private boolean hasCreatePayrollDeductionPerm;
    private boolean hasViewPayrollDeductionPerm;
    private boolean hasEditPayrollDeductionPerm;
    private boolean hasDeletePayrollDeductionPerm;
    private boolean hasCreateNotificationPerm;
    private boolean hasViewNotificationPerm;
    private boolean hasEditNotificationPerm;
    private boolean hasDeleteNotificationPerm;
    private boolean hasCreateAuditLogPerm;
    private boolean hasViewAuditLogPerm;
    private boolean hasEditAuditLogPerm;
    private boolean hasDeleteAuditLogPerm;
    private boolean hasCreateSystemSettingPerm;
    private boolean hasViewSystemSettingPerm;
    private boolean hasEditSystemSettingPerm;
    private boolean hasDeleteSystemSettingPerm;
    private boolean hasApproveLoanPerm;
    private boolean hasRejectLoanPerm;
    private boolean hasDisburseLoanPerm;
    private boolean hasVerifyDocumentPerm;
    private boolean hasPostPayrollBatchPerm;
    private boolean hasWaivePenaltyPerm;
    private boolean hasExportReportPerm;

    @PostConstruct
    public void init() {
        Object sessionUser = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("loggedInUser");
        if (sessionUser instanceof User) {
            setLoggedInUser((User) sessionUser);
        }
    }

    public void refreshPermissions() {
        if (loggedInUser == null || loggedInUser.getId() == null) {
            administrator = false;
            permissionCodes = Collections.emptySet();
        } else {
            administrator = userService.hasRole(loggedInUser.getId(), RoleConstants.ROLE_ADMINISTRATOR);
            permissionCodes = userService.getPermissionCodes(loggedInUser.getId());
        }
        hasCreateCompanyPerm = allowed(PermissionConstants.PERM_COMPANY_CREATE);
        hasViewCompanyPerm = allowed(PermissionConstants.PERM_COMPANY_VIEW);
        hasEditCompanyPerm = allowed(PermissionConstants.PERM_COMPANY_EDIT);
        hasDeleteCompanyPerm = allowed(PermissionConstants.PERM_COMPANY_DELETE);
        hasCreateEmployeePerm = allowed(PermissionConstants.PERM_EMPLOYEE_CREATE);
        hasViewEmployeePerm = allowed(PermissionConstants.PERM_EMPLOYEE_VIEW);
        hasEditEmployeePerm = allowed(PermissionConstants.PERM_EMPLOYEE_EDIT);
        hasDeleteEmployeePerm = allowed(PermissionConstants.PERM_EMPLOYEE_DELETE);
        hasCreateDocumentPerm = allowed(PermissionConstants.PERM_DOCUMENT_CREATE);
        hasViewDocumentPerm = allowed(PermissionConstants.PERM_DOCUMENT_VIEW);
        hasEditDocumentPerm = allowed(PermissionConstants.PERM_DOCUMENT_EDIT);
        hasDeleteDocumentPerm = allowed(PermissionConstants.PERM_DOCUMENT_DELETE);
        hasCreateUserPerm = allowed(PermissionConstants.PERM_USER_CREATE);
        hasViewUserPerm = allowed(PermissionConstants.PERM_USER_VIEW);
        hasEditUserPerm = allowed(PermissionConstants.PERM_USER_EDIT);
        hasDeleteUserPerm = allowed(PermissionConstants.PERM_USER_DELETE);
        hasCreateRolePerm = allowed(PermissionConstants.PERM_ROLE_CREATE);
        hasViewRolePerm = allowed(PermissionConstants.PERM_ROLE_VIEW);
        hasEditRolePerm = allowed(PermissionConstants.PERM_ROLE_EDIT);
        hasDeleteRolePerm = allowed(PermissionConstants.PERM_ROLE_DELETE);
        hasCreatePermissionPerm = allowed(PermissionConstants.PERM_PERMISSION_CREATE);
        hasViewPermissionPerm = allowed(PermissionConstants.PERM_PERMISSION_VIEW);
        hasEditPermissionPerm = allowed(PermissionConstants.PERM_PERMISSION_EDIT);
        hasDeletePermissionPerm = allowed(PermissionConstants.PERM_PERMISSION_DELETE);
        hasCreateLoanProductPerm = allowed(PermissionConstants.PERM_LOAN_PRODUCT_CREATE);
        hasViewLoanProductPerm = allowed(PermissionConstants.PERM_LOAN_PRODUCT_VIEW);
        hasEditLoanProductPerm = allowed(PermissionConstants.PERM_LOAN_PRODUCT_EDIT);
        hasDeleteLoanProductPerm = allowed(PermissionConstants.PERM_LOAN_PRODUCT_DELETE);
        hasCreateLoanPerm = allowed(PermissionConstants.PERM_LOAN_CREATE);
        hasViewLoanPerm = allowed(PermissionConstants.PERM_LOAN_VIEW);
        hasEditLoanPerm = allowed(PermissionConstants.PERM_LOAN_EDIT);
        hasDeleteLoanPerm = allowed(PermissionConstants.PERM_LOAN_DELETE);
        hasCreateLoanApprovalPerm = allowed(PermissionConstants.PERM_LOAN_APPROVAL_CREATE);
        hasViewLoanApprovalPerm = allowed(PermissionConstants.PERM_LOAN_APPROVAL_VIEW);
        hasEditLoanApprovalPerm = allowed(PermissionConstants.PERM_LOAN_APPROVAL_EDIT);
        hasDeleteLoanApprovalPerm = allowed(PermissionConstants.PERM_LOAN_APPROVAL_DELETE);
        hasCreateDisbursementPerm = allowed(PermissionConstants.PERM_DISBURSEMENT_CREATE);
        hasViewDisbursementPerm = allowed(PermissionConstants.PERM_DISBURSEMENT_VIEW);
        hasEditDisbursementPerm = allowed(PermissionConstants.PERM_DISBURSEMENT_EDIT);
        hasDeleteDisbursementPerm = allowed(PermissionConstants.PERM_DISBURSEMENT_DELETE);
        hasCreateRepaymentSchedulePerm = allowed(PermissionConstants.PERM_REPAYMENT_SCHEDULE_CREATE);
        hasViewRepaymentSchedulePerm = allowed(PermissionConstants.PERM_REPAYMENT_SCHEDULE_VIEW);
        hasEditRepaymentSchedulePerm = allowed(PermissionConstants.PERM_REPAYMENT_SCHEDULE_EDIT);
        hasDeleteRepaymentSchedulePerm = allowed(PermissionConstants.PERM_REPAYMENT_SCHEDULE_DELETE);
        hasCreateRepaymentPerm = allowed(PermissionConstants.PERM_REPAYMENT_CREATE);
        hasViewRepaymentPerm = allowed(PermissionConstants.PERM_REPAYMENT_VIEW);
        hasEditRepaymentPerm = allowed(PermissionConstants.PERM_REPAYMENT_EDIT);
        hasDeleteRepaymentPerm = allowed(PermissionConstants.PERM_REPAYMENT_DELETE);
        hasCreatePenaltyPerm = allowed(PermissionConstants.PERM_PENALTY_CREATE);
        hasViewPenaltyPerm = allowed(PermissionConstants.PERM_PENALTY_VIEW);
        hasEditPenaltyPerm = allowed(PermissionConstants.PERM_PENALTY_EDIT);
        hasDeletePenaltyPerm = allowed(PermissionConstants.PERM_PENALTY_DELETE);
        hasCreatePayrollBatchPerm = allowed(PermissionConstants.PERM_PAYROLL_BATCH_CREATE);
        hasViewPayrollBatchPerm = allowed(PermissionConstants.PERM_PAYROLL_BATCH_VIEW);
        hasEditPayrollBatchPerm = allowed(PermissionConstants.PERM_PAYROLL_BATCH_EDIT);
        hasDeletePayrollBatchPerm = allowed(PermissionConstants.PERM_PAYROLL_BATCH_DELETE);
        hasCreatePayrollDeductionPerm = allowed(PermissionConstants.PERM_PAYROLL_DEDUCTION_CREATE);
        hasViewPayrollDeductionPerm = allowed(PermissionConstants.PERM_PAYROLL_DEDUCTION_VIEW);
        hasEditPayrollDeductionPerm = allowed(PermissionConstants.PERM_PAYROLL_DEDUCTION_EDIT);
        hasDeletePayrollDeductionPerm = allowed(PermissionConstants.PERM_PAYROLL_DEDUCTION_DELETE);
        hasCreateNotificationPerm = allowed(PermissionConstants.PERM_NOTIFICATION_CREATE);
        hasViewNotificationPerm = allowed(PermissionConstants.PERM_NOTIFICATION_VIEW);
        hasEditNotificationPerm = allowed(PermissionConstants.PERM_NOTIFICATION_EDIT);
        hasDeleteNotificationPerm = allowed(PermissionConstants.PERM_NOTIFICATION_DELETE);
        hasCreateAuditLogPerm = allowed(PermissionConstants.PERM_AUDIT_LOG_CREATE);
        hasViewAuditLogPerm = allowed(PermissionConstants.PERM_AUDIT_LOG_VIEW);
        hasEditAuditLogPerm = allowed(PermissionConstants.PERM_AUDIT_LOG_EDIT);
        hasDeleteAuditLogPerm = allowed(PermissionConstants.PERM_AUDIT_LOG_DELETE);
        hasCreateSystemSettingPerm = allowed(PermissionConstants.PERM_SYSTEM_SETTING_CREATE);
        hasViewSystemSettingPerm = allowed(PermissionConstants.PERM_SYSTEM_SETTING_VIEW);
        hasEditSystemSettingPerm = allowed(PermissionConstants.PERM_SYSTEM_SETTING_EDIT);
        hasDeleteSystemSettingPerm = allowed(PermissionConstants.PERM_SYSTEM_SETTING_DELETE);
        hasApproveLoanPerm = allowed(PermissionConstants.PERM_LOAN_APPROVE);
        hasRejectLoanPerm = allowed(PermissionConstants.PERM_LOAN_REJECT);
        hasDisburseLoanPerm = allowed(PermissionConstants.PERM_LOAN_DISBURSE);
        hasVerifyDocumentPerm = allowed(PermissionConstants.PERM_DOCUMENT_VERIFY);
        hasPostPayrollBatchPerm = allowed(PermissionConstants.PERM_PAYROLL_BATCH_POST);
        hasWaivePenaltyPerm = allowed(PermissionConstants.PERM_PENALTY_WAIVE);
        hasExportReportPerm = allowed(PermissionConstants.PERM_REPORT_EXPORT);
    }

    private boolean allowed(String permissionCode) {
        return permissionCodes.contains(permissionCode);
    }

    public boolean hasPermission(String permissionCode) {
        return allowed(permissionCode);
    }

    public boolean hasRole(String roleName) {
        return loggedInUser != null && userService.hasRole(loggedInUser.getId(), roleName);
    }

    public boolean isActive(String viewId) {
        String currentView = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        return currentView != null && currentView.endsWith("/" + viewId);
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        refreshPermissions();
    }

    public boolean isAdministrator() {
        return administrator;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public String getSaveButtonStyle() {
        return saveButtonStyle;
    }

    public String getCancelButtonStyle() {
        return cancelButtonStyle;
    }

    public String getBackButtonStyle() {
        return backButtonStyle;
    }

    public String getSaveButtonIcon() {
        return saveButtonIcon;
    }

    public String getCancelButtonIcon() {
        return cancelButtonIcon;
    }

    public String getBackButtonIcon() {
        return backButtonIcon;
    }

    public boolean isHasCreateCompanyPerm() {
        return hasCreateCompanyPerm;
    }

    public boolean isHasViewCompanyPerm() {
        return hasViewCompanyPerm;
    }

    public boolean isHasEditCompanyPerm() {
        return hasEditCompanyPerm;
    }

    public boolean isHasDeleteCompanyPerm() {
        return hasDeleteCompanyPerm;
    }

    public boolean isHasCreateEmployeePerm() {
        return hasCreateEmployeePerm;
    }

    public boolean isHasViewEmployeePerm() {
        return hasViewEmployeePerm;
    }

    public boolean isHasEditEmployeePerm() {
        return hasEditEmployeePerm;
    }

    public boolean isHasDeleteEmployeePerm() {
        return hasDeleteEmployeePerm;
    }

    public boolean isHasCreateDocumentPerm() {
        return hasCreateDocumentPerm;
    }

    public boolean isHasViewDocumentPerm() {
        return hasViewDocumentPerm;
    }

    public boolean isHasEditDocumentPerm() {
        return hasEditDocumentPerm;
    }

    public boolean isHasDeleteDocumentPerm() {
        return hasDeleteDocumentPerm;
    }

    public boolean isHasCreateUserPerm() {
        return hasCreateUserPerm;
    }

    public boolean isHasViewUserPerm() {
        return hasViewUserPerm;
    }

    public boolean isHasEditUserPerm() {
        return hasEditUserPerm;
    }

    public boolean isHasDeleteUserPerm() {
        return hasDeleteUserPerm;
    }

    public boolean isHasCreateRolePerm() {
        return hasCreateRolePerm;
    }

    public boolean isHasViewRolePerm() {
        return hasViewRolePerm;
    }

    public boolean isHasEditRolePerm() {
        return hasEditRolePerm;
    }

    public boolean isHasDeleteRolePerm() {
        return hasDeleteRolePerm;
    }

    public boolean isHasCreatePermissionPerm() {
        return hasCreatePermissionPerm;
    }

    public boolean isHasViewPermissionPerm() {
        return hasViewPermissionPerm;
    }

    public boolean isHasEditPermissionPerm() {
        return hasEditPermissionPerm;
    }

    public boolean isHasDeletePermissionPerm() {
        return hasDeletePermissionPerm;
    }

    public boolean isHasCreateLoanProductPerm() {
        return hasCreateLoanProductPerm;
    }

    public boolean isHasViewLoanProductPerm() {
        return hasViewLoanProductPerm;
    }

    public boolean isHasEditLoanProductPerm() {
        return hasEditLoanProductPerm;
    }

    public boolean isHasDeleteLoanProductPerm() {
        return hasDeleteLoanProductPerm;
    }

    public boolean isHasCreateLoanPerm() {
        return hasCreateLoanPerm;
    }

    public boolean isHasViewLoanPerm() {
        return hasViewLoanPerm;
    }

    public boolean isHasEditLoanPerm() {
        return hasEditLoanPerm;
    }

    public boolean isHasDeleteLoanPerm() {
        return hasDeleteLoanPerm;
    }

    public boolean isHasCreateLoanApprovalPerm() {
        return hasCreateLoanApprovalPerm;
    }

    public boolean isHasViewLoanApprovalPerm() {
        return hasViewLoanApprovalPerm;
    }

    public boolean isHasEditLoanApprovalPerm() {
        return hasEditLoanApprovalPerm;
    }

    public boolean isHasDeleteLoanApprovalPerm() {
        return hasDeleteLoanApprovalPerm;
    }

    public boolean isHasCreateDisbursementPerm() {
        return hasCreateDisbursementPerm;
    }

    public boolean isHasViewDisbursementPerm() {
        return hasViewDisbursementPerm;
    }

    public boolean isHasEditDisbursementPerm() {
        return hasEditDisbursementPerm;
    }

    public boolean isHasDeleteDisbursementPerm() {
        return hasDeleteDisbursementPerm;
    }

    public boolean isHasCreateRepaymentSchedulePerm() {
        return hasCreateRepaymentSchedulePerm;
    }

    public boolean isHasViewRepaymentSchedulePerm() {
        return hasViewRepaymentSchedulePerm;
    }

    public boolean isHasEditRepaymentSchedulePerm() {
        return hasEditRepaymentSchedulePerm;
    }

    public boolean isHasDeleteRepaymentSchedulePerm() {
        return hasDeleteRepaymentSchedulePerm;
    }

    public boolean isHasCreateRepaymentPerm() {
        return hasCreateRepaymentPerm;
    }

    public boolean isHasViewRepaymentPerm() {
        return hasViewRepaymentPerm;
    }

    public boolean isHasEditRepaymentPerm() {
        return hasEditRepaymentPerm;
    }

    public boolean isHasDeleteRepaymentPerm() {
        return hasDeleteRepaymentPerm;
    }

    public boolean isHasCreatePenaltyPerm() {
        return hasCreatePenaltyPerm;
    }

    public boolean isHasViewPenaltyPerm() {
        return hasViewPenaltyPerm;
    }

    public boolean isHasEditPenaltyPerm() {
        return hasEditPenaltyPerm;
    }

    public boolean isHasDeletePenaltyPerm() {
        return hasDeletePenaltyPerm;
    }

    public boolean isHasCreatePayrollBatchPerm() {
        return hasCreatePayrollBatchPerm;
    }

    public boolean isHasViewPayrollBatchPerm() {
        return hasViewPayrollBatchPerm;
    }

    public boolean isHasEditPayrollBatchPerm() {
        return hasEditPayrollBatchPerm;
    }

    public boolean isHasDeletePayrollBatchPerm() {
        return hasDeletePayrollBatchPerm;
    }

    public boolean isHasCreatePayrollDeductionPerm() {
        return hasCreatePayrollDeductionPerm;
    }

    public boolean isHasViewPayrollDeductionPerm() {
        return hasViewPayrollDeductionPerm;
    }

    public boolean isHasEditPayrollDeductionPerm() {
        return hasEditPayrollDeductionPerm;
    }

    public boolean isHasDeletePayrollDeductionPerm() {
        return hasDeletePayrollDeductionPerm;
    }

    public boolean isHasCreateNotificationPerm() {
        return hasCreateNotificationPerm;
    }

    public boolean isHasViewNotificationPerm() {
        return hasViewNotificationPerm;
    }

    public boolean isHasEditNotificationPerm() {
        return hasEditNotificationPerm;
    }

    public boolean isHasDeleteNotificationPerm() {
        return hasDeleteNotificationPerm;
    }

    public boolean isHasCreateAuditLogPerm() {
        return hasCreateAuditLogPerm;
    }

    public boolean isHasViewAuditLogPerm() {
        return hasViewAuditLogPerm;
    }

    public boolean isHasEditAuditLogPerm() {
        return hasEditAuditLogPerm;
    }

    public boolean isHasDeleteAuditLogPerm() {
        return hasDeleteAuditLogPerm;
    }

    public boolean isHasCreateSystemSettingPerm() {
        return hasCreateSystemSettingPerm;
    }

    public boolean isHasViewSystemSettingPerm() {
        return hasViewSystemSettingPerm;
    }

    public boolean isHasEditSystemSettingPerm() {
        return hasEditSystemSettingPerm;
    }

    public boolean isHasDeleteSystemSettingPerm() {
        return hasDeleteSystemSettingPerm;
    }

    public boolean isHasApproveLoanPerm() {
        return hasApproveLoanPerm;
    }

    public boolean isHasRejectLoanPerm() {
        return hasRejectLoanPerm;
    }

    public boolean isHasDisburseLoanPerm() {
        return hasDisburseLoanPerm;
    }

    public boolean isHasVerifyDocumentPerm() {
        return hasVerifyDocumentPerm;
    }

    public boolean isHasPostPayrollBatchPerm() {
        return hasPostPayrollBatchPerm;
    }

    public boolean isHasWaivePenaltyPerm() {
        return hasWaivePenaltyPerm;
    }

    public boolean isHasExportReportPerm() {
        return hasExportReportPerm;
    }
}
