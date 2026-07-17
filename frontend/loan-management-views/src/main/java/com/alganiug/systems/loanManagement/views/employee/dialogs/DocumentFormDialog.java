package com.alganiug.systems.loanManagement.views.employee.dialogs;

import com.alganiug.systems.loanManagement.views.navigation.LoanManagementHyperLinks;
import com.alganiug.systems.loanManagement.core.services.employee.DocumentService;
import com.alganiug.systems.loanManagement.models.employee.Document;
import com.alganiug.systems.loanManagement.views.dialogs.DialogForm;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "documentFormDialog")
@ViewScoped
public class DocumentFormDialog extends DialogForm<Document> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{documentServiceImpl}")
    private DocumentService service;

    public DocumentFormDialog() {
        super(LoanManagementHyperLinks.DOCUMENT_FORM_DIALOG, 700, 550);
        resetModal();
    }

    @Override
    protected DocumentService getService() {
        return service;
    }

    public void setService(DocumentService service) {
        this.service = service;
    }

    @Override
    public void resetModal() {
        model = new Document();
        editing = false;
    }
}
