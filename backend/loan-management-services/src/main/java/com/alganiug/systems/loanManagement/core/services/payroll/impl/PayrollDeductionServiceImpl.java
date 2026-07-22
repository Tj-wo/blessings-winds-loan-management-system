package com.alganiug.systems.loanManagement.core.services.payroll.impl;

import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.ServiceValidationException;
import com.alganiug.systems.loanManagement.core.services.payroll.PayrollDeductionService;
import com.alganiug.systems.loanManagement.models.payroll.PayrollDeduction;
import com.alganiug.systems.loanManagement.models.loan.Loan;
import com.alganiug.systems.loanManagement.models.loan.Repayment;
import com.alganiug.systems.loanManagement.models.security.User;
import com.alganiug.systems.loanManagement.models.security.RoleConstants;
import com.alganiug.systems.loanManagement.models.constants.DeductionStatus;
import com.alganiug.systems.loanManagement.models.constants.LoanStatus;
import com.alganiug.systems.loanManagement.models.constants.RepaymentStatus;
import com.alganiug.systems.loanManagement.models.constants.PayrollStatus;
import com.alganiug.systems.loanManagement.models.payroll.PayrollDeductionBatch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.time.LocalDate;
import java.math.BigDecimal;

@Service
public class PayrollDeductionServiceImpl extends GenericServiceImpl<PayrollDeduction>
        implements PayrollDeductionService {

    public PayrollDeductionServiceImpl() {
        super(PayrollDeduction.class);
    }

    @Override
    public PayrollDeduction saveInstance(PayrollDeduction deduction) {
        requirePresent(deduction, "Payroll deduction");
        if (deduction.getId() != null) {
            PayrollDeduction existing = entityManager.find(PayrollDeduction.class, deduction.getId());
            if (existing == null) throw new ServiceValidationException("Payroll deduction was not found");
            if (!isReconciliationEditable(existing.getStatus())) {
                throw new ServiceValidationException("A repaid or received deduction can no longer be edited");
            }
        }
        requirePresent(deduction.getBatch(), "Payroll batch");
        requirePresent(deduction.getLoan(), "Loan");
        PayrollDeductionBatch managedBatch = entityManager.find(PayrollDeductionBatch.class, deduction.getBatch().getId());
        Loan managedLoan = entityManager.find(Loan.class, deduction.getLoan().getId());
        if (managedBatch == null) throw new ServiceValidationException("Payroll batch was not found");
        if (managedLoan == null) throw new ServiceValidationException("Loan was not found");
        deduction.setBatch(managedBatch);
        deduction.setLoan(managedLoan);
        deduction.setEmployee(managedLoan.getEmployee());
        return super.saveInstance(deduction);
    }
    @Override
    @Transactional(readOnly = true)
    public List<PayrollDeduction> getForCompany(UUID companyId) {
        if (companyId == null) return Collections.emptyList();
        return entityManager.createQuery("select payrolldeduction from PayrollDeduction payrolldeduction join fetch payrolldeduction.batch join fetch payrolldeduction.employee join fetch payrolldeduction.loan where payrolldeduction.batch.company.id = :companyId and payrolldeduction.recordStatus = com.alganiug.systems.loanManagement.models.constants.RecordStatus.ACTIVE order by payrolldeduction.createdAt desc", PayrollDeduction.class)
                .setParameter("companyId", companyId).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PayrollDeduction> getAllWithDetails() {
        return entityManager.createQuery(
                        "select deduction from PayrollDeduction deduction join fetch deduction.batch "
                                + "join fetch deduction.employee join fetch deduction.loan "
                                + "where deduction.recordStatus = com.alganiug.systems.loanManagement.models.constants.RecordStatus.ACTIVE "
                                + "order by deduction.createdAt desc", PayrollDeduction.class)
                .getResultList();
    }
    @Override
    public PayrollDeduction markRepaid(PayrollDeduction deduction, User supervisor) {
        PayrollDeduction managed = managedDeduction(deduction);
        requireRole(supervisor, RoleConstants.ROLE_HR_SUPERVISOR, "Only a supervisor can mark a deduction as repaid");
        if (supervisor.getCompany() == null || !sameEntity(supervisor.getCompany(), managed.getEmployee().getCompany())) {
            throw new ServiceValidationException("A supervisor can only update deductions for their company");
        }
        if (managed.getStatus() != DeductionStatus.PENDING && managed.getStatus() != DeductionStatus.PARTIAL
                && managed.getStatus() != DeductionStatus.UNMATCHED) {
            throw new ServiceValidationException("Only a pending deduction can be marked as repaid");
        }
        requirePositive(managed.getDeductedAmount(), "Deducted amount");
        managed.setStatus(DeductionStatus.REPAID);
        recordAudit("DEDUCTION_REPAID", managed);
        return entityManager.merge(managed);
    }

    @Override
    public PayrollDeduction confirmReceived(PayrollDeduction deduction, User administrator) {
        PayrollDeduction managed = managedDeduction(deduction);
        requireRole(administrator, RoleConstants.ROLE_ADMINISTRATOR, "Only an administrator can confirm receipt");
        if (managed.getStatus() != DeductionStatus.REPAID) {
            throw new ServiceValidationException("The supervisor must mark this deduction as repaid first");
        }
        Loan loan = managed.getLoan();
        BigDecimal outstanding = loan.getOutstandingBalance() == null ? BigDecimal.ZERO : loan.getOutstandingBalance();
        BigDecimal applied = managed.getDeductedAmount().min(outstanding);
        requirePositive(applied, "Repayment amount");

        Repayment repayment = new Repayment();
        repayment.setPayrollDeduction(managed);
        repayment.setLoan(loan);
        repayment.setAmount(applied);
        repayment.setPaymentDate(LocalDate.now());
        repayment.setPaymentReference("PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        repayment.setStatus(RepaymentStatus.POSTED);
        entityManager.persist(repayment);
        managed.setRepayment(repayment);
        managed.setStatus(DeductionStatus.RECEIVED);

        BigDecimal amountPaid = loan.getAmountPaid() == null ? BigDecimal.ZERO : loan.getAmountPaid();
        loan.setAmountPaid(amountPaid.add(applied));
        loan.setOutstandingBalance(outstanding.subtract(applied).max(BigDecimal.ZERO));
        loan.setStatus(loan.getOutstandingBalance().signum() == 0 ? LoanStatus.COMPLETED : LoanStatus.ACTIVE);
        entityManager.merge(loan);
        PayrollDeductionBatch batch = managed.getBatch();
        BigDecimal receivedTotal = batch.getReceivedTotal() == null ? BigDecimal.ZERO : batch.getReceivedTotal();
        batch.setReceivedTotal(receivedTotal.add(applied));
        batch.setStatus(batch.getReceivedTotal().compareTo(batch.getExpectedTotal()) >= 0
                ? PayrollStatus.POSTED : PayrollStatus.PARTIALLY_POSTED);
        entityManager.merge(batch);
        recordAudit("DEDUCTION_RECEIVED", managed);
        return entityManager.merge(managed);
    }

    private PayrollDeduction managedDeduction(PayrollDeduction deduction) {
        requirePresent(deduction, "Payroll deduction");
        return getInstanceById(deduction.getId())
                .orElseThrow(() -> new ServiceValidationException("Payroll deduction was not found"));
    }

    private void requireRole(User user, String roleName, String message) {
        if (user == null || user.getRoles().stream().noneMatch(role -> roleName.equals(role.getName()))) {
            throw new ServiceValidationException(message);
        }
    }
    @Override
    protected boolean isDeletable(PayrollDeduction deduction) {
        PayrollDeduction managed = deduction == null || deduction.getId() == null
                ? null : entityManager.find(PayrollDeduction.class, deduction.getId());
        return managed != null && isReconciliationEditable(managed.getStatus());
    }

    private boolean isReconciliationEditable(DeductionStatus status) {
        return status == DeductionStatus.PENDING || status == DeductionStatus.PARTIAL
                || status == DeductionStatus.UNMATCHED;
    }
    @Override
    protected void validate(PayrollDeduction deduction) {
        requirePresent(deduction.getBatch(), "Payroll batch");
        requirePresent(deduction.getEmployee(), "Employee");
        requirePresent(deduction.getLoan(), "Loan");
        if (!sameEntity(deduction.getBatch().getCompany(), deduction.getEmployee().getCompany())) {
            throw new ServiceValidationException("Payroll batch and employee must belong to the same company");
        }
        if (!sameEntity(deduction.getEmployee(), deduction.getLoan().getEmployee())) {
            throw new ServiceValidationException("Payroll deduction loan must belong to the employee");
        }
        requireNonNegative(deduction.getExpectedAmount(), "Expected deduction amount");
        requireNonNegative(deduction.getDeductedAmount(), "Deducted amount");
        requirePresent(deduction.getStatus(), "Reconciliation status");
    }
}
