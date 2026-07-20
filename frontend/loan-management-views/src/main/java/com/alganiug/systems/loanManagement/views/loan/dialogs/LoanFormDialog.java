package com.alganiug.systems.loanManagement.views.loan.dialogs;

import com.alganiug.systems.loanManagement.core.services.loan.LoanProductService;
import com.alganiug.systems.loanManagement.core.services.loan.LoanService;
import com.alganiug.systems.loanManagement.models.constants.DisbursementMethod;
import com.alganiug.systems.loanManagement.models.loan.Loan;
import com.alganiug.systems.loanManagement.models.loan.LoanProduct;
import com.alganiug.systems.loanManagement.models.security.User;
import com.alganiug.systems.loanManagement.views.controllers.AuthenticationController;
import com.alganiug.systems.loanManagement.views.dialogs.DialogForm;
import com.alganiug.systems.loanManagement.views.dialogs.MessageComposer;
import com.alganiug.systems.loanManagement.views.navigation.LoanManagementHyperLinks;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ManagedBean(name = "loanFormDialog")
@ViewScoped
public class LoanFormDialog extends DialogForm<Loan> {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(LoanFormDialog.class.getName());

    @ManagedProperty(value = "#{loanServiceImpl}")
    private LoanService service;

    @ManagedProperty(value = "#{loanProductServiceImpl}")
    private LoanProductService loanProductService;

    @ManagedProperty(value = "#{authenticationController}")
    private AuthenticationController authenticationController;

    public LoanFormDialog() {
        super(LoanManagementHyperLinks.LOAN_FORM_DIALOG, 760, 560);
        resetModal();
    }

    @Override
    protected LoanService getService() {
        return service;
    }

    @Override
    public void persist() {
        bindBorrower();
        super.persist();
    }

    public String submitApplication() {
        try {
            persist();
            MessageComposer.info("Application submitted",
                    "Your loan reference is " + model.getLoanReference() + ". Upload the required KYC documents next.");
            resetModal();
            return LoanManagementHyperLinks.EMPLOYEE_DASHBOARD;
        } catch (RuntimeException exception) {
            LOGGER.log(Level.SEVERE, "Failed to submit loan application", exception);
            MessageComposer.error("Unable to submit application", exception.getMessage());
            return null;
        }
    }

    private void bindBorrower() {
        User user = authenticationController == null ? null : authenticationController.getLoggedInUser();
        if (user == null || user.getEmployee() == null) {
            throw new IllegalStateException("Only an activated employee account can apply for a loan");
        }
        model.setEmployee(user.getEmployee());
        model.setCompany(user.getEmployee().getCompany());
    }

    public List<LoanProduct> getProducts() {
        return loanProductService == null ? Collections.emptyList() : loanProductService.getAllInstances();
    }

    public DisbursementMethod[] getDisbursementMethods() {
        return DisbursementMethod.values();
    }

    @Override
    public void resetModal() {
        model = new Loan();
        editing = false;
    }

    public void setService(LoanService service) { this.service = service; }
    public void setLoanProductService(LoanProductService loanProductService) {
        this.loanProductService = loanProductService;
    }
    public void setAuthenticationController(AuthenticationController authenticationController) {
        this.authenticationController = authenticationController;
    }
}