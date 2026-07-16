package com.alganiug.systems.loanManagement.models.employee;

import com.alganiug.systems.loanManagement.models.base.BaseEntity;
import com.alganiug.systems.loanManagement.models.constants.*;
import com.alganiug.systems.loanManagement.models.loan.Loan;
import com.alganiug.systems.loanManagement.models.security.User;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "documents", indexes = { @Index(name = "idx_document_employee", columnList = "employee_id"),
        @Index(name = "idx_document_loan", columnList = "loan_id") })
public class Document extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)

    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id")

    private Loan loan;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false, length = 40)

    private DocumentType documentType;

    @Column(name = "original_filename", nullable = false, length = 255)

    private String originalFilename;

    @Column(name = "mime_type", nullable = false, length = 100)

    private String mimeType;

    @Column(name = "content_size", nullable = false)

    private long contentSize;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "file_content", nullable = false, columnDefinition = "LONGBLOB")

    private byte[] fileContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false, length = 30)

    private DocumentStatus verificationStatus = DocumentStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")

    private User reviewedBy;

    @Column(name = "reviewed_at")

    private LocalDateTime reviewedAt;

    @Column(name = "verification_comment", length = 1000)

    private String verificationComment;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee v) {
        employee = v;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan v) {
        loan = v;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType v) {
        documentType = v;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String v) {
        originalFilename = v;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String v) {
        mimeType = v;
    }

    public long getContentSize() {
        return contentSize;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] v) {
        fileContent = v;
        contentSize = v == null ? 0 : v.length;
    }

    public DocumentStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(DocumentStatus v) {
        verificationStatus = v;
    }

    public User getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(User v) {
        reviewedBy = v;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime v) {
        reviewedAt = v;
    }

    public String getVerificationComment() {
        return verificationComment;
    }

    public void setVerificationComment(String v) {
        verificationComment = v;
    }
}
