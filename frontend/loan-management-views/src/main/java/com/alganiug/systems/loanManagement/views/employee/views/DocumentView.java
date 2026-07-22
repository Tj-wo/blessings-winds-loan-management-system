package com.alganiug.systems.loanManagement.views.employee.views;

import com.alganiug.systems.loanManagement.core.services.employee.DocumentService;
import com.alganiug.systems.loanManagement.models.constants.DocumentStatus;
import com.alganiug.systems.loanManagement.models.employee.Document;
import com.alganiug.systems.loanManagement.models.security.RoleConstants;
import com.alganiug.systems.loanManagement.models.security.User;
import com.alganiug.systems.loanManagement.views.EntityView;
import com.alganiug.systems.loanManagement.views.controllers.AuthenticationController;
import com.alganiug.systems.loanManagement.views.dialogs.MessageComposer;
import com.alganiug.systems.loanManagement.views.dialogs.UserMessageResolver;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.Collections;

@ManagedBean(name = "documentView")
@ViewScoped
public class DocumentView extends EntityView<Document> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{documentServiceImpl}")
    private DocumentService service;

    @ManagedProperty(value = "#{authenticationController}")
    private AuthenticationController authenticationController;

    @Override
    protected DocumentService getService() {
        return service;
    }

    @Override
    public void reload() {
        User user = authenticationController == null ? null : authenticationController.getLoggedInUser();
        if (user != null && user.getEmployee() != null) {
            setRecords(service.getDocumentsForEmployee(user.getEmployee().getId()));
            return;
        }
        if (user != null && hasRole(user, RoleConstants.ROLE_ADMINISTRATOR)) {
            setRecords(service.getKycDocumentsForReview(null));
            return;
        }
        if (user != null && hasRole(user, RoleConstants.ROLE_HR_SUPERVISOR)) {
            setRecords(user.getCompany() == null ? Collections.emptyList()
                    : service.getKycDocumentsForReview(user.getCompany().getId()));
            return;
        }
        setRecords(Collections.emptyList());
    }

    public void approve(Document document) {
        service.reviewDocument(document, DocumentStatus.VERIFIED, authenticationController.getLoggedInUser());
        MessageComposer.info("KYC approved", document.getOriginalFilename() + " has been verified.");
        reload();
    }

    public void reject(Document document) {
        service.reviewDocument(document, DocumentStatus.REJECTED, authenticationController.getLoggedInUser());
        MessageComposer.warn("KYC rejected", document.getOriginalFilename() + " has been rejected.");
        reload();
    }

    @Override
    public void delete(Document document) {
        try {
            service.deleteOwnedDocument(document, authenticationController.getLoggedInUser());
            MessageComposer.info("Document deleted", "Your document was deleted successfully.");
            reload();
        } catch (RuntimeException exception) {
            MessageComposer.error("Unable to delete document", UserMessageResolver.resolve(exception));
        }
    }

    public boolean isOwner(Document document) {
        User user = authenticationController == null ? null : authenticationController.getLoggedInUser();
        return user != null && user.getEmployee() != null && document != null && document.getEmployee() != null
                && user.getEmployee().getId().equals(document.getEmployee().getId());
    }

    private boolean hasRole(User user, String roleName) {
        return user.getRoles().stream().anyMatch(role -> roleName.equals(role.getName()));
    }

    public void setAuthenticationController(AuthenticationController authenticationController) {
        this.authenticationController = authenticationController;
    }

    public void setService(DocumentService service) {
        this.service = service;
    }
}