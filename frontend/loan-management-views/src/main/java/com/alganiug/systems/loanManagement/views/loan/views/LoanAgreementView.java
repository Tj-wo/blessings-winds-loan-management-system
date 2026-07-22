package com.alganiug.systems.loanManagement.views.loan.views;

import com.alganiug.systems.loanManagement.core.services.loan.LoanService;
import com.alganiug.systems.loanManagement.core.services.employee.DocumentService;
import com.alganiug.systems.loanManagement.models.employee.Document;
import com.alganiug.systems.loanManagement.models.loan.Loan;
import com.alganiug.systems.loanManagement.models.security.User;
import com.alganiug.systems.loanManagement.views.controllers.AuthenticationController;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.UUID;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ManagedBean(name = "loanAgreementView")
@ViewScoped
public class LoanAgreementView implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{loanServiceImpl}")
    private LoanService loanService;

    @ManagedProperty(value = "#{documentServiceImpl}")
    private DocumentService documentService;

    @ManagedProperty(value = "#{authenticationController}")
    private AuthenticationController authenticationController;

    private String loanId;
    private Loan loan;

    public void load() {
        if (loanId == null || loanId.trim().isEmpty()) {
            loan = null;
            return;
        }
        try {
            loan = loanService.getLoanDetails(UUID.fromString(loanId)).orElse(null);
            User user = authenticationController.getLoggedInUser();
            if (loan != null && user != null && user.getEmployee() != null
                    && !user.getEmployee().getId().equals(loan.getEmployee().getId())) {
                loan = null;
            }
        } catch (IllegalArgumentException ignored) {
            loan = null;
        }
    }

    public String getLoanId() { return loanId; }
    public void setLoanId(String loanId) { this.loanId = loanId; }
    public Loan getLoan() { return loan; }
    public List<Document> getAttachedKycDocuments() {
        if (loan == null || documentService == null) {
            return Collections.emptyList();
        }
        return documentService.getDocumentsForEmployee(loan.getEmployee().getId()).stream()
                .filter(document -> document.getLoan() != null && loan.getId().equals(document.getLoan().getId()))
                .filter(document -> document.getDocumentType() != com.alganiug.systems.loanManagement.models.constants.DocumentType.SIGNED_LOAN_AGREEMENT)
                .collect(Collectors.toList());
    }
    public boolean isImage(Document document) {
        return document != null && document.getMimeType() != null && document.getMimeType().startsWith("image/");
    }
    public boolean isPdf(Document document) {
        return document != null && "application/pdf".equalsIgnoreCase(document.getMimeType());
    }
    public void setLoanService(LoanService loanService) { this.loanService = loanService; }
    public void setDocumentService(DocumentService documentService) { this.documentService = documentService; }
    public void setAuthenticationController(AuthenticationController authenticationController) {
        this.authenticationController = authenticationController;
    }
}