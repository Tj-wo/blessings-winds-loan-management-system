package com.alganiug.systems.loanManagement.core.services.payroll;

import com.alganiug.systems.loanManagement.core.services.GenericService;
import com.alganiug.systems.loanManagement.models.payroll.PayrollDeduction;
import java.util.List;
import java.util.UUID;

public interface PayrollDeductionService extends GenericService<PayrollDeduction> {
    List<PayrollDeduction> getForCompany(UUID companyId);
}
