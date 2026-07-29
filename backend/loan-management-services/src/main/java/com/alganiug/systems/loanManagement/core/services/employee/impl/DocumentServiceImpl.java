package com.alganiug.systems.loanManagement.core.services.employee.impl;

import com.alganiug.systems.loanManagement.core.services.ServiceValidationException;
import com.alganiug.systems.loanManagement.core.services.employee.DocumentService;
import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.models.constants.DocumentStatus;
import com.alganiug.systems.loanManagement.models.constants.DocumentType;
import com.alganiug.systems.loanManagement.models.constants.KycStatus;
import com.alganiug.systems.loanManagement.models.constants.RecordStatus;
import com.alganiug.systems.loanManagement.models.employee.Document;
import com.alganiug.systems.loanManagement.models.employee.Employee;
import com.alganiug.systems.loanManagement.models.security.RoleConstants;
import com.alganiug.systems.loanManagement.models.security.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

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
                                + "and document.recordStatus = :recordStatus order by document.createdAt desc", Document.class)
                .setParameter("employeeId", employeeId)
                .setParameter("recordStatus", RecordStatus.ACTIVE)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Document> getKycDocumentsForReview(UUID companyId) {
        String scope = companyId == null ? "" : "and document.employee.company.id = :companyId ";
        javax.persistence.TypedQuery<Document> query = entityManager.createQuery(
                        "select document from Document document join fetch document.employee employee "
                                + "join fetch employee.company where document.loan is null "
                                + "and document.documentType <> :agreementType " + scope
                                + "and document.recordStatus = :recordStatus order by document.createdAt desc", Document.class)
                .setParameter("agreementType", DocumentType.SIGNED_LOAN_AGREEMENT)
                .setParameter("recordStatus", RecordStatus.ACTIVE);
        if (companyId != null) {
            query.setParameter("companyId", companyId);
        }
        return query.getResultList();
    }

    @Override
    public Document reviewDocument(Document document, DocumentStatus status, User reviewer) {
        requirePresent(document, "Document");
        requirePresent(status, "Verification status");
        requirePresent(reviewer, "Reviewer");
        if (status != DocumentStatus.VERIFIED && status != DocumentStatus.REJECTED) {
            throw new ServiceValidationException("A review must approve or reject the document");
        }
        Document managed = getInstanceById(document.getId())
                .orElseThrow(() -> new ServiceValidationException("Document was not found"));
        if (managed.getLoan() != null || managed.getDocumentType() == DocumentType.SIGNED_LOAN_AGREEMENT) {
            throw new ServiceValidationException("Only original employee KYC documents can be reviewed here");
        }
        if (!hasRole(reviewer, RoleConstants.ROLE_ADMINISTRATOR)) {
            throw new ServiceValidationException("Only a system administrator can review KYC documents");
        }
        managed.setVerificationStatus(status);
        managed.setReviewedBy(entityManager.getReference(User.class, reviewer.getId()));
        managed.setReviewedAt(LocalDateTime.now());
        Document saved = entityManager.merge(managed);
        updateEmployeeKycStatus(managed.getEmployee().getId());
        recordAudit("KYC_" + status.name(), saved);
        return saved;
    }

    @Override
    public void deleteOwnedDocument(Document document, User owner) {
        requirePresent(document, "Document");
        requirePresent(owner, "Document owner");
        Document managed = getInstanceById(document.getId())
                .orElseThrow(() -> new ServiceValidationException("Document was not found"));
        if (managed.getLoan() != null || managed.getDocumentType() == DocumentType.SIGNED_LOAN_AGREEMENT) {
            throw new ServiceValidationException("Only original employee KYC documents can be deleted here");
        }
        if (owner.getEmployee() == null || !sameEntity(owner.getEmployee(), managed.getEmployee())) {
            throw new ServiceValidationException("Only the employee who owns this document can delete it");
        }
        super.deleteInstance(managed);
        if (managed.getLoan() == null) {
            managed.getEmployee().setKycStatus(KycStatus.PENDING_VERIFICATION);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Document> getDocumentForPreview(UUID documentId, User viewer) {
        if (documentId == null || viewer == null) return Optional.empty();
        List<Document> matches = entityManager.createQuery(
                        "select document from Document document join fetch document.employee employee "
                                + "join fetch employee.company left join fetch document.loan "
                                + "where document.id = :documentId and document.recordStatus = :recordStatus", Document.class)
                .setParameter("documentId", documentId)
                .setParameter("recordStatus", RecordStatus.ACTIVE)
                .getResultList();
        if (matches.isEmpty()) return Optional.empty();
        Document document = matches.get(0);
        boolean owner = viewer.getEmployee() != null && sameEntity(viewer.getEmployee(), document.getEmployee());
        boolean administrator = hasRole(viewer, RoleConstants.ROLE_ADMINISTRATOR);
        return owner || administrator ? Optional.of(document) : Optional.empty();
    }
    @Override
    protected void validate(Document document) {
        requirePresent(document.getEmployee(), "Employee");
        requirePresent(document.getDocumentType(), "Document type");
        if (document.getDocumentType() == DocumentType.SIGNED_LOAN_AGREEMENT && document.getLoan() == null) {
            throw new ServiceValidationException("A signed loan agreement must be attached to its loan application");
        }
        if (document.getDocumentType() != DocumentType.SIGNED_LOAN_AGREEMENT && document.getLoan() != null) {
            throw new ServiceValidationException("KYC documents belong to the employee and must not select a loan application");
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

    private boolean hasRole(User user, String roleName) {
        return user.getRoles().stream().anyMatch(role -> roleName.equals(role.getName()));
    }

    private void updateEmployeeKycStatus(UUID employeeId) {
        List<Document> documents = entityManager.createQuery(
                        "select document from Document document where document.employee.id = :employeeId "
                                + "and document.loan is null and document.recordStatus = :recordStatus", Document.class)
                .setParameter("employeeId", employeeId)
                .setParameter("recordStatus", RecordStatus.ACTIVE)
                .getResultList();
        boolean rejected = documents.stream()
                .anyMatch(document -> document.getVerificationStatus() == DocumentStatus.REJECTED);
        boolean allVerified = Arrays.asList(DocumentType.NATIONAL_ID_FRONT, DocumentType.NATIONAL_ID_BACK,
                        DocumentType.PASSPORT_PHOTO, DocumentType.EMPLOYMENT_LETTER,
                        DocumentType.RECENT_PAYSLIP, DocumentType.BANK_CONFIRMATION).stream()
                .allMatch(type -> documents.stream().anyMatch(document -> document.getDocumentType() == type
                        && document.getVerificationStatus() == DocumentStatus.VERIFIED));
        Employee employee = entityManager.find(Employee.class, employeeId);
        employee.setKycStatus(rejected ? KycStatus.REJECTED
                : allVerified ? KycStatus.APPROVED : KycStatus.PENDING_VERIFICATION);
    }
}