package com.alganiug.systems.loanManagement.core.services.loan.impl;

import com.alganiug.systems.loanManagement.core.services.ServiceValidationException;
import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.loan.LoanService;
import com.alganiug.systems.loanManagement.models.constants.*;
import com.alganiug.systems.loanManagement.models.employee.Document;
import com.alganiug.systems.loanManagement.models.loan.Loan;
import com.alganiug.systems.loanManagement.models.notification.Notification;
import com.alganiug.systems.loanManagement.models.security.RoleConstants;
import com.alganiug.systems.loanManagement.models.security.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class LoanServiceImpl extends GenericServiceImpl<Loan> implements LoanService {

    private static final Set<DocumentType> REQUIRED_KYC_DOCUMENTS = EnumSet.of(
            DocumentType.NATIONAL_ID_FRONT,
            DocumentType.NATIONAL_ID_BACK,
            DocumentType.PASSPORT_PHOTO,
            DocumentType.EMPLOYMENT_LETTER,
            DocumentType.RECENT_PAYSLIP,
            DocumentType.BANK_CONFIRMATION);

    public LoanServiceImpl() {
        super(Loan.class);
    }

    @Override
    public Loan saveInstance(Loan loan) {
        boolean creating = loan != null && loan.getId() == null;
        List<Document> reusableDocuments = creating ? requireCompleteKyc(loan) : Collections.emptyList();
        Loan saved = super.saveInstance(loan);
        if (creating) {
            attachKycDocuments(saved, reusableDocuments);
            notifyCompanySupervisors(saved);
        }
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Loan> getLoansForEmployee(UUID employeeId) {
        if (employeeId == null) {
            return Collections.emptyList();
        }
        return detailedLoanQuery("loan.employee.id = :scopeId")
                .setParameter("scopeId", employeeId)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Loan> getLoansForCompany(UUID companyId) {
        if (companyId == null) {
            return Collections.emptyList();
        }
        return detailedLoanQuery("loan.company.id = :scopeId")
                .setParameter("scopeId", companyId)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Loan> getAllLoansWithDetails() {
        return entityManager.createQuery(
                        "select loan from Loan loan join fetch loan.employee join fetch loan.company "
                                + "join fetch loan.product where loan.recordStatus = :recordStatus "
                                + "order by loan.createdAt desc", Loan.class)
                .setParameter("recordStatus", RecordStatus.ACTIVE)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Loan> getLoanDetails(UUID loanId) {
        if (loanId == null) {
            return Optional.empty();
        }
        return entityManager.createQuery(
                        "select loan from Loan loan join fetch loan.employee join fetch loan.company "
                                + "join fetch loan.product where loan.id = :loanId "
                                + "and loan.recordStatus = :recordStatus", Loan.class)
                .setParameter("loanId", loanId)
                .setParameter("recordStatus", RecordStatus.ACTIVE)
                .getResultStream()
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveLoans(UUID companyId) {
        String jpql = "select count(loan) from Loan loan where loan.recordStatus = :recordStatus "
                + "and loan.status in (:statuses)";
        if (companyId != null) {
            jpql += " and loan.company.id = :companyId";
        }
        javax.persistence.TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class)
                .setParameter("recordStatus", RecordStatus.ACTIVE)
                .setParameter("statuses", Arrays.asList(LoanStatus.DISBURSED, LoanStatus.ACTIVE));
        if (companyId != null) {
            query.setParameter("companyId", companyId);
        }
        return query.getSingleResult();
    }
    @Override
    public Loan disburse(Loan loan) {
        requirePresent(loan, "Loan");
        Loan managedLoan = getInstanceById(loan.getId())
                .orElseThrow(() -> new ServiceValidationException("Loan was not found"));
        if (managedLoan.getStatus() != LoanStatus.APPROVED) {
            throw new ServiceValidationException("Only a loan with final administrator approval can be disbursed");
        }
        managedLoan.setStatus(LoanStatus.DISBURSED);
        managedLoan.setDisbursedOn(LocalDate.now());
        Loan saved = entityManager.merge(managedLoan);
        recordAudit("LOAN_DISBURSED", saved);
        notifyEmployee(saved, "Loan disbursed",
                "Your application " + saved.getLoanReference() + " has been disbursed.");
        return saved;
    }

    @Override
    protected void validate(Loan loan) {
        prepareApplicationTerms(loan);
        requireText(loan.getLoanReference(), "Loan reference");
        requirePresent(loan.getEmployee(), "Employee");
        requirePresent(loan.getCompany(), "Company");
        requirePresent(loan.getProduct(), "Loan product");
        if (!sameEntity(loan.getCompany(), loan.getEmployee().getCompany())) {
            throw new ServiceValidationException("Loan company must match the employee company");
        }
        requirePositive(loan.getRequestedAmount(), "Requested amount");
        if (loan.getRequestedAmount().compareTo(loan.getProduct().getMinimumAmount()) < 0
                || loan.getRequestedAmount().compareTo(loan.getProduct().getMaximumAmount()) > 0) {
            throw new ServiceValidationException("Requested amount is outside the product limits");
        }
        if (loan.getRequestedTermMonths() < loan.getProduct().getMinimumTermMonths()
                || loan.getRequestedTermMonths() > loan.getProduct().getMaximumTermMonths()) {
            throw new ServiceValidationException("Requested term is outside the product limits");
        }
        requireText(loan.getPurpose(), "Loan purpose");
        requirePresent(loan.getPreferredDisbursementMethod(), "Preferred disbursement method");
        requireNonNegative(loan.getMonthlyInterestRate(), "Monthly interest rate");
        requireNonNegative(loan.getInterestAmount(), "Interest amount");
        requireNonNegative(loan.getInstallmentAmount(), "Installment amount");
        requireNonNegative(loan.getTotalPayable(), "Total payable");
        requireNonNegative(loan.getAmountPaid(), "Amount paid");
        requireNonNegative(loan.getOutstandingBalance(), "Outstanding balance");
        requirePresent(loan.getStatus(), "Loan status");
    }

    private javax.persistence.TypedQuery<Loan> detailedLoanQuery(String scopeCondition) {
        return entityManager.createQuery(
                        "select loan from Loan loan join fetch loan.employee join fetch loan.company "
                                + "join fetch loan.product where " + scopeCondition
                                + " and loan.recordStatus = :recordStatus order by loan.createdAt desc", Loan.class)
                .setParameter("recordStatus", RecordStatus.ACTIVE);
    }

    private List<Document> requireCompleteKyc(Loan loan) {
        requirePresent(loan, "Loan application");
        requirePresent(loan.getEmployee(), "Employee");
        List<Document> documents = entityManager.createQuery(
                        "select document from Document document where document.employee.id = :employeeId "
                                + "and document.loan is null and document.recordStatus = :recordStatus", Document.class)
                .setParameter("employeeId", loan.getEmployee().getId())
                .setParameter("recordStatus", RecordStatus.ACTIVE)
                .getResultList();
        Set<DocumentType> missing = EnumSet.copyOf(REQUIRED_KYC_DOCUMENTS);
        for (Document document : documents) {
            missing.remove(document.getDocumentType());
        }
        if (!missing.isEmpty()) {
            throw new ServiceValidationException("Complete KYC before applying. Missing documents: " + missing);
        }
        return documents;
    }

    private void attachKycDocuments(Loan loan, List<Document> reusableDocuments) {
        for (Document source : reusableDocuments) {
            if (!REQUIRED_KYC_DOCUMENTS.contains(source.getDocumentType())) {
                continue;
            }
            Document attached = new Document();
            attached.setEmployee(loan.getEmployee());
            attached.setLoan(loan);
            attached.setDocumentType(source.getDocumentType());
            attached.setOriginalFilename(source.getOriginalFilename());
            attached.setMimeType(source.getMimeType());
            attached.setFileContent(source.getFileContent());
            attached.setVerificationStatus(source.getVerificationStatus());
            attached.setReviewedBy(source.getReviewedBy());
            attached.setReviewedAt(source.getReviewedAt());
            attached.setVerificationComment(source.getVerificationComment());
            entityManager.persist(attached);
        }
    }

    private void notifyCompanySupervisors(Loan loan) {
        List<User> supervisors = entityManager.createQuery(
                        "select distinct user from User user join user.roles role "
                                + "where user.company.id = :companyId and role.name = :roleName", User.class)
                .setParameter("companyId", loan.getCompany().getId())
                .setParameter("roleName", RoleConstants.ROLE_HR_SUPERVISOR)
                .getResultList();
        for (User supervisor : supervisors) {
            createNotification(supervisor, "Loan awaiting supervisor review",
                    loan.getLoanReference() + " has been submitted for review.");
        }
    }

    private void notifyEmployee(Loan loan, String title, String message) {
        List<User> users = entityManager.createQuery(
                        "select user from User user where user.employee.id = :employeeId", User.class)
                .setParameter("employeeId", loan.getEmployee().getId())
                .getResultList();
        for (User user : users) {
            createNotification(user, title, message);
        }
    }

    private void createNotification(User recipient, String title, String message) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setStatus(NotificationStatus.SENT);
        notification.setSentAt(LocalDateTime.now());
        entityManager.persist(notification);
    }

    private void prepareApplicationTerms(Loan loan) {
        if (loan == null || loan.getId() != null || loan.getProduct() == null
                || loan.getRequestedAmount() == null || loan.getRequestedTermMonths() < 1) {
            return;
        }
        if (loan.getLoanReference() == null || loan.getLoanReference().trim().isEmpty()) {
            loan.setLoanReference("BWL-" + LocalDate.now().getYear() + "-"
                    + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        loan.setRequestedOn(LocalDate.now());
        loan.setStatus(LoanStatus.SUBMITTED);
        loan.setMonthlyInterestRate(loan.getProduct().getMonthlyInterestRate());
        BigDecimal months = BigDecimal.valueOf(loan.getRequestedTermMonths());
        BigDecimal monthlyRate = loan.getMonthlyInterestRate()
                .divide(BigDecimal.valueOf(100), 8, RoundingMode.HALF_UP);
        BigDecimal interest = loan.getRequestedAmount().multiply(monthlyRate).multiply(months)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = loan.getRequestedAmount().add(interest).setScale(2, RoundingMode.HALF_UP);
        loan.setInterestAmount(interest);
        loan.setTotalPayable(total);
        loan.setInstallmentAmount(total.divide(months, 2, RoundingMode.HALF_UP));
        loan.setAmountPaid(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        loan.setOutstandingBalance(total);
    }
}
