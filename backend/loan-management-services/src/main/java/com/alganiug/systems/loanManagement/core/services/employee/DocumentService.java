package com.alganiug.systems.loanManagement.core.services.employee;

import com.alganiug.systems.loanManagement.core.services.GenericService;
import com.alganiug.systems.loanManagement.models.constants.DocumentStatus;
import com.alganiug.systems.loanManagement.models.employee.Document;
import com.alganiug.systems.loanManagement.models.security.User;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface DocumentService extends GenericService<Document> {
    List<Document> getDocumentsForEmployee(UUID employeeId);
    List<Document> getKycDocumentsForReview(UUID companyId);
    Document reviewDocument(Document document, DocumentStatus status, User reviewer);
    void deleteOwnedDocument(Document document, User owner);
    Optional<Document> getDocumentForPreview(UUID documentId, User viewer);
}