package com.alganiug.systems.loanManagement.views.loan.views;

import com.alganiug.systems.loanManagement.core.services.loan.LoanService;
import com.alganiug.systems.loanManagement.models.loan.Loan;
import com.alganiug.systems.loanManagement.views.EntityView;
import com.alganiug.systems.loanManagement.views.dialogs.MessageComposer;
import com.alganiug.systems.loanManagement.views.controllers.AuthenticationController;
import com.alganiug.systems.loanManagement.models.security.User;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.logging.Level;
import java.util.logging.Logger;

@ManagedBean(name = "loanView")
@ViewScoped
public class LoanView extends EntityView<Loan> {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(LoanView.class.getName());

    @ManagedProperty(value = "#{loanServiceImpl}")
    private LoanService service;

    @ManagedProperty(value = "#{authenticationController}")
    private AuthenticationController authenticationController;

    @Override
    protected LoanService getService() {
        return service;
    }

    @Override
    public void reload() {
        User user = authenticationController == null ? null : authenticationController.getLoggedInUser();
        if (user != null && user.getEmployee() != null) {
            java.util.List<Loan> employeeLoans = service.getLoansForEmployee(user.getEmployee().getId());
            setScopedRecords(employeeLoans);
            return;
        }
        if (user != null && user.getCompany() != null) {
            setScopedRecords(service.getLoansForCompany(user.getCompany().getId()));
            return;
        }
        setScopedRecords(service.getAllLoansWithDetails());
    }

    private void setScopedRecords(java.util.List<Loan> employeeLoans) {
        setRecords(employeeLoans);
    }

    public long getActiveLoanCount() {
        User user = authenticationController == null ? null : authenticationController.getLoggedInUser();
        java.util.UUID companyId = user != null && user.getCompany() != null
                ? user.getCompany().getId()
                : null;
        return service == null ? 0 : service.countActiveLoans(companyId);
    }
    public void disburse(Loan loan) {
        try {
            service.disburse(loan);
            MessageComposer.info("Loan disbursed", "The loan was disbursed successfully.");
            reload();
        } catch (RuntimeException exception) {
            LOGGER.log(Level.SEVERE, "Unable to disburse loan "
                    + (loan == null ? "" : loan.getLoanReference()), exception);
            MessageComposer.error("Unable to disburse loan", exception.getMessage());
        }
    }
    public void setAuthenticationController(AuthenticationController authenticationController) {
        this.authenticationController = authenticationController;
    }

    public void setService(LoanService service) {
        this.service = service;
    }
}
