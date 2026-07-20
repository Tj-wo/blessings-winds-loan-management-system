package com.alganiug.systems.loanManagement.core.services.employee.impl;

import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.ServiceValidationException;
import com.alganiug.systems.loanManagement.core.services.employee.DocumentService;
import com.alganiug.systems.loanManagement.models.employee.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentServiceImpl extends GenericServiceImpl<Document> implements DocumentService {

    @Value("${loan.documents.max-upload-bytes:10485760}")
    private long maximumUploadBytes;

    public DocumentServiceImpl() {
        super(Document.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Document> getDocumentsForEmployee(UUID employeeId) {
        if (employeeId == null) {
            return Collections.emptyList();
        }
        return entityManager.createQuery(
                        "select document from Document document join fetch document.employee "
                                + "left join fetch document.loan where document.employee.id = :employeeId "
                                + "and document.recordStatus = com.alganiug.systems.loanManagement.models.constants.RecordStatus.ACTIVE "
                                + "order by document.createdAt desc", Document.class)
                .setParameter("employeeId", employeeId)
                .getResultList();
    }
    @Override
    protected void validate(Document document) {
        requirePresent(document.getEmployee(), "Employee");
        requirePresent(document.getDocumentType(), "Document type");
        if (document.getDocumentType() == com.alganiug.systems.loanManagement.models.constants.DocumentType.SIGNED_LOAN_AGREEMENT
                && document.getLoan() == null) {
            throw new ServiceValidationException("A signed loan agreement must be attached to its loan application");
        }
        requireText(document.getOriginalFilename(), "Original filename");
        requireText(document.getMimeType(), "MIME type");
        if (document.getFileContent() == null || document.getFileContent().length == 0) {
            throw new ServiceValidationException("Document content is required");
        }
        if (document.getFileContent().length > maximumUploadBytes) {
            throw new ServiceValidationException("Document exceeds the configured upload limit");
        }
        if (document.getContentSize() != document.getFileContent().length) {
            throw new ServiceValidationException("Document content size does not match its data");
        }
        if (document.getLoan() != null && !sameEntity(document.getEmployee(), document.getLoan().getEmployee())) {
            throw new ServiceValidationException("Loan document must belong to the loan employee");
        }
    }
}
