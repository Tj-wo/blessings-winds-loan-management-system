package com.alganiug.systems.loanManagement.core.services.loan.impl;

import com.alganiug.systems.loanManagement.core.services.ServiceValidationException;
import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.loan.LoanApprovalService;
import com.alganiug.systems.loanManagement.models.constants.*;
import com.alganiug.systems.loanManagement.models.employee.Document;
import com.alganiug.systems.loanManagement.models.loan.Loan;
import com.alganiug.systems.loanManagement.models.loan.LoanApproval;
import com.alganiug.systems.loanManagement.models.notification.Notification;
import com.alganiug.systems.loanManagement.models.security.RoleConstants;
import com.alganiug.systems.loanManagement.models.security.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Service
public class LoanApprovalServiceImpl extends GenericServiceImpl<LoanApproval> implements LoanApprovalService {

    private static final Set<DocumentType> REQUIRED_APPLICATION_DOCUMENTS = EnumSet.of(
            DocumentType.NATIONAL_ID_FRONT,
            DocumentType.NATIONAL_ID_BACK,
            DocumentType.PASSPORT_PHOTO,
            DocumentType.EMPLOYMENT_LETTER,
            DocumentType.RECENT_PAYSLIP,
            DocumentType.BANK_CONFIRMATION);

    public LoanApprovalServiceImpl() {
        super(LoanApproval.class);
    }

    @Override
    public LoanApproval saveInstance(LoanApproval approval) {
        requirePresent(approval, "Approval");
        requirePresent(approval.getLoan(), "Loan application");
        requirePresent(approval.getDecidedBy(), "Decision maker");

        Loan loan = entityManager.find(Loan.class, approval.getLoan().getId());
        if (loan == null) {
            throw new ServiceValidationException("Loan application was not found");
        }

        ApprovalStage stage = resolveStage(approval.getDecidedBy());
        if (stage == ApprovalStage.HR_REVIEW) {
            if (approval.getDecidedBy().getCompany() == null
                    || !sameEntity(approval.getDecidedBy().getCompany(), loan.getCompany())) {
                throw new ServiceValidationException("A supervisor can only review loans for their own company");
            }
        }
        approval.setApprovalStage(stage);
        validateStage(stage, loan);
        approval.setLoan(loan);

        LoanApproval saved = super.saveInstance(approval);
        applyDecision(saved, loan);
        entityManager.merge(loan);
        recordAudit("LOAN_" + saved.getDecision(), loan);
        notifyWorkflowParticipants(saved, loan);
        return saved;
    }

    @Override
    protected void validate(LoanApproval approval) {
        requirePresent(approval.getApprovalStage(), "Approval stage");
        requirePresent(approval.getDecision(), "Decision");
        requirePresent(approval.getDecidedBy(), "Decision maker");
        requireText(approval.getComment(), "Decision comment");

        if (approval.getDecidedAt() == null) {
            approval.setDecidedAt(LocalDateTime.now());
        }

        if (approval.getDecision() == ApprovalDecision.APPROVED) {
            validateOffer(approval);
            validateApplicationDocuments(approval.getLoan());
        }
    }

    private ApprovalStage resolveStage(User actor) {
        if (hasRole(actor, RoleConstants.ROLE_HR_SUPERVISOR)) {
            return ApprovalStage.HR_REVIEW;
        }
        if (hasRole(actor, RoleConstants.ROLE_ADMINISTRATOR)
                || hasRole(actor, RoleConstants.ROLE_LOAN_MANAGER)) {
            return ApprovalStage.ADMIN_REVIEW;
        }
        throw new ServiceValidationException("You are not allowed to review loan applications");
    }

    private void validateStage(ApprovalStage stage, Loan loan) {
        if (stage == ApprovalStage.HR_REVIEW
                && loan.getStatus() != LoanStatus.SUBMITTED
                && loan.getStatus() != LoanStatus.HR_REVIEW) {
            throw new ServiceValidationException("This application is not awaiting supervisor review");
        }
        if (stage == ApprovalStage.ADMIN_REVIEW && loan.getStatus() != LoanStatus.HR_APPROVED) {
            throw new ServiceValidationException("Administrator review is available only after supervisor approval");
        }
    }

    private void validateOffer(LoanApproval approval) {
        BigDecimal maximumAmount = approval.getApprovalStage() == ApprovalStage.ADMIN_REVIEW
                && approval.getLoan().getApprovedAmount() != null
                ? approval.getLoan().getApprovedAmount()
                : approval.getLoan().getRequestedAmount();
        BigDecimal offeredAmount = approval.getOfferedAmount() == null ? maximumAmount : approval.getOfferedAmount();
        int maximumTerm = approval.getApprovalStage() == ApprovalStage.ADMIN_REVIEW
                && approval.getLoan().getApprovedTermMonths() != null
                ? approval.getLoan().getApprovedTermMonths()
                : approval.getLoan().getRequestedTermMonths();
        int offeredTerm = approval.getOfferedTermMonths() == null ? maximumTerm : approval.getOfferedTermMonths();

        if (offeredAmount.compareTo(BigDecimal.ZERO) <= 0 || offeredAmount.compareTo(maximumAmount) > 0) {
            throw new ServiceValidationException("Approved amount must be positive and cannot exceed the amount currently offered");
        }
        if (offeredTerm < 1 || offeredTerm > maximumTerm) {
            throw new ServiceValidationException("Approved term cannot exceed the term currently offered");
        }
        approval.setOfferedAmount(offeredAmount);
        approval.setOfferedTermMonths(offeredTerm);
    }

    private void applyDecision(LoanApproval approval, Loan loan) {
        if (approval.getDecision() == ApprovalDecision.REJECTED) {
            loan.setStatus(LoanStatus.REJECTED);
            return;
        }
        if (approval.getDecision() == ApprovalDecision.RETURNED_FOR_INFORMATION) {
            loan.setStatus(LoanStatus.CHANGES_REQUESTED);
            return;
        }

        loan.setApprovedAmount(approval.getOfferedAmount());
        loan.setApprovedTermMonths(approval.getOfferedTermMonths());
        recalculateTerms(loan);
        if (approval.getApprovalStage() == ApprovalStage.HR_REVIEW) {
            loan.setStatus(LoanStatus.HR_APPROVED);
        } else {
            loan.setStatus(LoanStatus.APPROVED);
            loan.setApprovedOn(LocalDate.now());
        }
    }

    private void recalculateTerms(Loan loan) {
        BigDecimal months = BigDecimal.valueOf(loan.getApprovedTermMonths());
        BigDecimal monthlyRate = loan.getMonthlyInterestRate()
                .divide(BigDecimal.valueOf(100), 8, RoundingMode.HALF_UP);
        BigDecimal interest = loan.getApprovedAmount().multiply(monthlyRate).multiply(months)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = loan.getApprovedAmount().add(interest).setScale(2, RoundingMode.HALF_UP);
        loan.setInterestAmount(interest);
        loan.setTotalPayable(total);
        loan.setInstallmentAmount(total.divide(months, 2, RoundingMode.HALF_UP));
        loan.setOutstandingBalance(total.subtract(loan.getAmountPaid()).max(BigDecimal.ZERO));
    }

    private void validateApplicationDocuments(Loan loan) {
        Set<DocumentType> attachedTypes = EnumSet.noneOf(DocumentType.class);
        attachedTypes.addAll(entityManager.createQuery(
                        "select distinct document.documentType from Document document "
                                + "where document.loan.id = :loanId and document.recordStatus = :recordStatus",
                        DocumentType.class)
                .setParameter("loanId", loan.getId())
                .setParameter("recordStatus", RecordStatus.ACTIVE)
                .getResultList());
        Set<DocumentType> missingTypes = EnumSet.copyOf(REQUIRED_APPLICATION_DOCUMENTS);
        missingTypes.removeAll(attachedTypes);
        if (!missingTypes.isEmpty()) {
            throw new ServiceValidationException("The application cannot be approved. Missing documents: " + missingTypes);
        }
    }

    private boolean hasRole(User user, String roleName) {
        Long count = entityManager.createQuery(
                        "select count(role) from User account join account.roles role "
                                + "where account.id = :userId and role.name = :roleName", Long.class)
                .setParameter("userId", user.getId())
                .setParameter("roleName", roleName)
                .getSingleResult();
        return count > 0;
    }

    private void notifyWorkflowParticipants(LoanApproval approval, Loan loan) {
        notifyEmployee(loan, "Loan application " + approval.getDecision(),
                "Your application " + loan.getLoanReference() + " was "
                        + approval.getDecision().name().toLowerCase().replace('_', ' ') + ". " + approval.getComment());
        if (approval.getDecision() == ApprovalDecision.APPROVED
                && approval.getApprovalStage() == ApprovalStage.HR_REVIEW) {
            notifyRole(RoleConstants.ROLE_ADMINISTRATOR, null, "Loan awaiting final approval",
                    loan.getLoanReference() + " was approved by the supervisor and requires administrator review.");
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

    private void notifyRole(String roleName, java.util.UUID companyId, String title, String message) {
        String jpql = "select distinct user from User user join user.roles role where role.name = :roleName";
        if (companyId != null) {
            jpql += " and user.company.id = :companyId";
        }
        javax.persistence.TypedQuery<User> query = entityManager.createQuery(jpql, User.class)
                .setParameter("roleName", roleName);
        if (companyId != null) {
            query.setParameter("companyId", companyId);
        }
        for (User user : query.getResultList()) {
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
}
