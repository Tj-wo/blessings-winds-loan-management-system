package com.alganiug.systems.loanManagement.web.security;

import com.alganiug.systems.loanManagement.core.services.security.UserService;
import com.alganiug.systems.loanManagement.core.services.audit.AuditContext;
import com.alganiug.systems.loanManagement.models.security.RoleConstants;
import com.alganiug.systems.loanManagement.models.security.User;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class PageAuthorizationFilter implements Filter {

    private UserService userService;

    @Override
    public void init(FilterConfig filterConfig) {
        userService = WebApplicationContextUtils
                .getRequiredWebApplicationContext(filterConfig.getServletContext())
                .getBean(UserService.class);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = requestPath(httpRequest);

        if (isPublic(path)) {
            chain.doFilter(request, response);
            return;
        }

        Object sessionUser = httpRequest.getSession(false) == null
                ? null
                : httpRequest.getSession(false).getAttribute("loggedInUser");
        if (!(sessionUser instanceof User)) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/pages/external/login.xhtml");
            return;
        }

        User user = (User) sessionUser;
        if (!isAuthorized(user, path)) {
            httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isAuthorized(User user, String path) {
        boolean employee = userService.hasRole(user.getId(), RoleConstants.ROLE_EMPLOYEE);
        boolean hrSupervisor = userService.hasRole(user.getId(), RoleConstants.ROLE_HR_SUPERVISOR);
        boolean loanManager = userService.hasRole(user.getId(), RoleConstants.ROLE_LOAN_MANAGER);
        boolean administrator = userService.hasRole(user.getId(), RoleConstants.ROLE_ADMINISTRATOR);

        if (path.equals("/pages/dashboard/EmployeeDashboard.xhtml")) {
            return employee;
        }
        if (path.equals("/pages/dashboard/HrDashboard.xhtml")) {
            return hrSupervisor;
        }
        if (path.equals("/pages/dashboard/Dashboard.xhtml")) {
            return administrator || loanManager;
        }
        if (path.equals("/pages/loan/LoanApplication.xhtml")) {
            return employee;
        }

        if (path.startsWith("/documents/content/")) {
            return true;
        }

        if (employee) {
            return isEmployeePage(path);
        }

        Set<String> permissions = userService.getPermissionCodes(user.getId());
        return isBackOfficePageAllowed(path, permissions, administrator, loanManager, hrSupervisor);
    }

    private boolean isEmployeePage(String path) {
        return path.equals("/pages/loan/LoanView.xhtml")
                || path.equals("/pages/loan/LoanAgreement.xhtml")
                || path.equals("/pages/loan/LoanDetails.xhtml")
                || path.equals("/pages/loan/LoanStatement.xhtml")
                || path.equals("/pages/loan/LoanFormDialog.xhtml")
                || path.equals("/pages/employee/DocumentView.xhtml")
                || path.equals("/pages/employee/DocumentFormDialog.xhtml")
                || path.equals("/pages/security/ProfileView.xhtml")
                || path.equals("/pages/notification/NotificationView.xhtml");
    }

    private boolean isBackOfficePageAllowed(String path, Set<String> permissions, boolean administrator,
                                             boolean loanManager, boolean hrSupervisor) {
        if (path.endsWith("FormDialog.xhtml")) {
            return isDialogAllowed(path, permissions);
        }        if (path.equals("/pages/security/ProfileView.xhtml")) {
            return true;
        }
        if (path.startsWith("/pages/security/")) {
            return administrator && hasAny(permissions, "USER_VIEW", "ROLE_VIEW", "PERMISSION_VIEW");
        }
        if (path.startsWith("/pages/company/")) {
            return permissions.contains("COMPANY_VIEW");
        }
        if (path.startsWith("/pages/employee/")) {
            return permissions.contains("EMPLOYEE_VIEW") || permissions.contains("DOCUMENT_VIEW");
        }
        if (path.startsWith("/pages/payroll/")) {
            return (hrSupervisor || administrator) && hasAny(permissions, "PAYROLL_BATCH_VIEW", "PAYROLL_DEDUCTION_VIEW");
        }
        if (path.startsWith("/pages/loan/")) {
            return (administrator || loanManager || hrSupervisor) && permissions.contains("LOAN_VIEW");
        }
        if (path.startsWith("/pages/notification/")) {
            return permissions.contains("NOTIFICATION_VIEW");
        }
        if (path.startsWith("/pages/reports/")) {
            return permissions.contains("REPORT_EXPORT");
        }
        if (path.startsWith("/pages/settings/")) {
            return administrator && permissions.contains("SYSTEM_SETTING_VIEW");
        }
        if (path.startsWith("/pages/audit/")) {
            return administrator && permissions.contains("AUDIT_LOG_VIEW");
        }
        return false;
    }

    private boolean isDialogAllowed(String path, Set<String> permissions) {
        if (path.contains("CompanyForm")) return hasAny(permissions, "COMPANY_CREATE", "COMPANY_EDIT");
        if (path.contains("EmployeeForm")) return hasAny(permissions, "EMPLOYEE_CREATE", "EMPLOYEE_EDIT");
        if (path.contains("DocumentForm")) return hasAny(permissions, "DOCUMENT_CREATE", "DOCUMENT_EDIT");
        if (path.contains("UserForm")) return hasAny(permissions, "USER_CREATE", "USER_EDIT");
        if (path.contains("RoleForm")) return hasAny(permissions, "ROLE_CREATE", "ROLE_EDIT");
        if (path.contains("LoanProductForm")) return hasAny(permissions, "LOAN_PRODUCT_CREATE", "LOAN_PRODUCT_EDIT");
        if (path.contains("LoanApprovalForm")) return permissions.contains("LOAN_APPROVAL_CREATE");
        if (path.contains("PayrollDeductionBatchForm")) return permissions.contains("PAYROLL_BATCH_CREATE");
        if (path.contains("PayrollDeductionForm")) return hasAny(permissions, "PAYROLL_DEDUCTION_CREATE", "PAYROLL_DEDUCTION_EDIT");
        if (path.contains("NotificationForm")) return permissions.contains("NOTIFICATION_CREATE");
        if (path.contains("SystemSettingForm")) return hasAny(permissions, "SYSTEM_SETTING_CREATE", "SYSTEM_SETTING_EDIT");
        return false;
    }
    private boolean hasAny(Set<String> permissions, String... requiredPermissions) {
        for (String permission : requiredPermissions) {
            if (permissions.contains(permission)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPublic(String path) {
        return path.isEmpty()
                || path.equals("/")
                || path.equals("/index.xhtml")
                || path.startsWith("/javax.faces.resource/")
                || path.startsWith("/resources/")
                || path.startsWith("/pages/external/")
                || path.startsWith("/pages/error/");
    }

    private String requestPath(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        return uri.substring(contextPath.length());
    }
}