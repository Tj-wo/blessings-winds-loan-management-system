package com.alganiug.systems.loanManagement.views.converters;

import com.alganiug.systems.loanManagement.core.services.payroll.PayrollDeductionBatchService;
import com.alganiug.systems.loanManagement.models.payroll.PayrollDeductionBatch;
import javax.faces.convert.FacesConverter;

@FacesConverter("payrollDeductionBatchConverter")
public class PayrollDeductionBatchConverter extends EntityConverter<PayrollDeductionBatch> {
    public PayrollDeductionBatchConverter() {
        super("#{payrollDeductionBatchServiceImpl}", PayrollDeductionBatchService.class);
    }
}