package com.alganiug.systems.loanManagement.core.services.payroll;

import com.alganiug.systems.loanManagement.core.services.GenericService;
import com.alganiug.systems.loanManagement.models.payroll.PayrollDeductionBatch;
import java.util.List;
import java.util.UUID;

public interface PayrollDeductionBatchService extends GenericService<PayrollDeductionBatch> {
    List<PayrollDeductionBatch> getForCompany(UUID companyId);
}
