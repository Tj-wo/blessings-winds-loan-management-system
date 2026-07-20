package com.alganiug.systems.loanManagement.views.loan.dialogs;

import com.alganiug.systems.loanManagement.core.services.loan.LoanApprovalService;
import com.alganiug.systems.loanManagement.core.services.loan.LoanService;
import com.alganiug.systems.loanManagement.models.constants.ApprovalDecision;
import com.alganiug.systems.loanManagement.models.constants.LoanStatus;
import com.alganiug.systems.loanManagement.models.loan.Loan;
import com.alganiug.systems.loanManagement.models.loan.LoanApproval;
import com.alganiug.systems.loanManagement.models.security.User;
import com.alganiug.systems.loanManagement.views.controllers.AuthenticationController;
import com.alganiug.systems.loanManagement.views.dialogs.DialogForm;
import com.alganiug.systems.loanManagement.views.navigation.LoanManagementHyperLinks;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ManagedBean(name = "loanApprovalFormDialog")
@ViewScoped
public class LoanApprovalFormDialog extends DialogForm<LoanApproval> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{loanApprovalServiceImpl}")
    private LoanApprovalService service;

    @ManagedProperty(value = "#{loanServiceImpl}")
    private LoanService loanService;

    @ManagedProperty(value = "#{authenticationController}")
    private AuthenticationController authenticationController;

    public LoanApprovalFormDialog() {
        super(LoanManagementHyperLinks.LOAN_APPROVAL_FORM_DIALOG, 780, 560);
        resetModal();
    }

    @Override
    protected LoanApprovalService getService() {
        return service;
    }

    public String review(Loan loan) {
        if (loan == null || loan.getId() == null) {
            return null;
        }

        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("contentHeight", 560);
        options.put("contentWidth", 780);

        Map<String, List<String>> parameters = new HashMap<>();
        parameters.put("loanId", Collections.singletonList(loan.getId().toString()));
        PrimeFaces.current().dialog().openDynamic(getName(), options, parameters);
        return null;
    }

    @PostConstruct
    public void initializeSelectedLoan() {
        String loanId = FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("loanId");
        if (loanId == null || loanService == null) {
            return;
        }
        try {
            loanService.getLoanDetails(UUID.fromString(loanId)).ifPresent(model::setLoan);
        } catch (IllegalArgumentException ignored) {
            model.setLoan(null);
        }
    }

    public boolean canReview(Loan loan) {
        if (loan == null || authenticationController == null) {
            return false;
        }
        User user = authenticationController.getLoggedInUser();
        if (user == null) {
            return false;
        }
        if (user.getCompany() != null) {
            return loan.getStatus() == LoanStatus.SUBMITTED || loan.getStatus() == LoanStatus.HR_REVIEW;
        }
        return loan.getStatus() == LoanStatus.HR_APPROVED;
    }

    @Override
    public void persist() {
        model.setDecidedBy(authenticationController.getLoggedInUser());
        super.persist();
    }

    public ApprovalDecision[] getDecisions() {
        return ApprovalDecision.values();
    }

    @Override
    public void resetModal() {
        model = new LoanApproval();
        editing = false;
    }

    public void setService(LoanApprovalService service) {
        this.service = service;
    }

    public void setLoanService(LoanService loanService) {
        this.loanService = loanService;
    }

    public void setAuthenticationController(AuthenticationController authenticationController) {
        this.authenticationController = authenticationController;
    }
}
