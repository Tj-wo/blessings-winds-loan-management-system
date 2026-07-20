package com.alganiug.systems.loanManagement.views.employee.dialogs;

import com.alganiug.systems.loanManagement.core.services.employee.DocumentService;
import com.alganiug.systems.loanManagement.core.services.loan.LoanService;
import com.alganiug.systems.loanManagement.models.constants.DocumentType;
import com.alganiug.systems.loanManagement.models.employee.Document;
import com.alganiug.systems.loanManagement.models.loan.Loan;
import com.alganiug.systems.loanManagement.models.security.User;
import com.alganiug.systems.loanManagement.views.controllers.AuthenticationController;
import com.alganiug.systems.loanManagement.views.dialogs.DialogForm;
import com.alganiug.systems.loanManagement.views.dialogs.MessageComposer;
import com.alganiug.systems.loanManagement.views.navigation.LoanManagementHyperLinks;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

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

@ManagedBean(name = "documentFormDialog")
@ViewScoped
public class DocumentFormDialog extends DialogForm<Document> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{documentServiceImpl}")
    private DocumentService service;

    @ManagedProperty(value = "#{loanServiceImpl}")
    private LoanService loanService;

    @ManagedProperty(value = "#{authenticationController}")
    private AuthenticationController authenticationController;

    public DocumentFormDialog() {
        super(LoanManagementHyperLinks.DOCUMENT_FORM_DIALOG, 760, 520);
        resetModal();
    }

    @Override
    protected DocumentService getService() {
        return service;
    }

    public String uploadSignedAgreement(Loan loan) {
        if (loan == null || loan.getId() == null) {
            return null;
        }

        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("contentHeight", 520);
        options.put("contentWidth", 760);

        Map<String, List<String>> parameters = new HashMap<>();
        parameters.put("loanId", Collections.singletonList(loan.getId().toString()));
        parameters.put("documentType", Collections.singletonList(DocumentType.SIGNED_LOAN_AGREEMENT.name()));
        PrimeFaces.current().dialog().openDynamic(getName(), options, parameters);
        return null;
    }

    @PostConstruct
    public void initializeApplicationDocument() {
        String loanId = FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("loanId");
        if (loanId == null || loanService == null) {
            return;
        }

        try {
            loanService.getLoanDetails(UUID.fromString(loanId)).ifPresent(loan -> {
                model.setLoan(loan);
                model.setEmployee(loan.getEmployee());
                model.setDocumentType(DocumentType.SIGNED_LOAN_AGREEMENT);
            });
        } catch (IllegalArgumentException ignored) {
            model.setLoan(null);
        }
    }

    public boolean isSignedAgreementUpload() {
        return model != null
                && model.getLoan() != null
                && model.getDocumentType() == DocumentType.SIGNED_LOAN_AGREEMENT;
    }

    public void handleUpload(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        model.setOriginalFilename(file.getFileName());
        model.setMimeType(file.getContentType());
        model.setFileContent(file.getContent());
        MessageComposer.info("File selected", file.getFileName());
    }

    public DocumentType[] getDocumentTypes() {
        return DocumentType.values();
    }

    public List<Loan> getEmployeeLoans() {
        User user = authenticationController == null ? null : authenticationController.getLoggedInUser();
        return user == null || user.getEmployee() == null
                ? Collections.emptyList()
                : loanService.getLoansForEmployee(user.getEmployee().getId());
    }

    @Override
    public void resetModal() {
        model = new Document();
        User user = authenticationController == null ? null : authenticationController.getLoggedInUser();
        if (user != null && user.getEmployee() != null) {
            model.setEmployee(user.getEmployee());
        }
        editing = false;
    }

    @Override
    public void persist() {
        User user = authenticationController.getLoggedInUser();
        if (model.getEmployee() == null && user != null) {
            model.setEmployee(user.getEmployee());
        }
        super.persist();
    }

    public void setService(DocumentService service) {
        this.service = service;
    }

    public void setLoanService(LoanService loanService) {
        this.loanService = loanService;
    }

    public void setAuthenticationController(AuthenticationController authenticationController) {
        this.authenticationController = authenticationController;
        resetModal();
    }
}
