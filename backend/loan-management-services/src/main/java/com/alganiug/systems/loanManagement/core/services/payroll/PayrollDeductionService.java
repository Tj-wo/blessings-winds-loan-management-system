package com.alganiug.systems.loanManagement.core.services.payroll;

import com.alganiug.systems.loanManagement.core.services.GenericService;
import com.alganiug.systems.loanManagement.models.payroll.PayrollDeduction;
import com.alganiug.systems.loanManagement.models.security.User;
import java.util.List;
import java.util.UUID;

public interface PayrollDeductionService extends GenericService<PayrollDeduction> {
    List<PayrollDeduction> getForCompany(UUID companyId);
    List<PayrollDeduction> getAllWithDetails();
    PayrollDeduction markRepaid(PayrollDeduction deduction, User supervisor);
    PayrollDeduction confirmReceived(PayrollDeduction deduction, User administrator);
}
