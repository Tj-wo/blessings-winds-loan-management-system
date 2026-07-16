package com.alganiug.systems.loanManagement.views.employee.views;

import com.alganiug.systems.loanManagement.core.services.employee.DocumentService;
import com.alganiug.systems.loanManagement.models.employee.Document;
import com.alganiug.systems.loanManagement.views.EntityView;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "documentView")
@ViewScoped
public class DocumentView extends EntityView<Document> {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{documentServiceImpl}")
    private DocumentService service;

    @Override
    protected DocumentService getService() {
        return service;
    }

    public void setService(DocumentService service) {
        this.service = service;
    }
}
