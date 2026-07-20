package com.alganiug.systems.loanManagement.core.services.loan.impl;

import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.ServiceValidationException;
import com.alganiug.systems.loanManagement.core.services.loan.LoanProductService;
import com.alganiug.systems.loanManagement.models.loan.LoanProduct;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class LoanProductServiceImpl extends GenericServiceImpl<LoanProduct> implements LoanProductService {

    public LoanProductServiceImpl() {
        super(LoanProduct.class);
    }

    @Override
    protected void validate(LoanProduct product) {
        requireText(product.getName(), "Product name");
        requireNonNegative(product.getMonthlyInterestRate(), "Monthly interest rate");
        requirePositive(product.getMinimumAmount(), "Minimum amount");
        requirePositive(product.getMaximumAmount(), "Maximum amount");
        if (product.getMaximumAmount().compareTo(product.getMinimumAmount()) < 0) {
            throw new ServiceValidationException("Maximum amount cannot be less than minimum amount");
        }
        if (product.getMinimumTermMonths() < 1 || product.getMaximumTermMonths() < product.getMinimumTermMonths()) {
            throw new ServiceValidationException("Loan product term limits are invalid");
        }
        if (product.getMaximumSalaryDeductionPercent() == null) {
            product.setMaximumSalaryDeductionPercent(new BigDecimal("100"));
        }
        if (product.getMaximumSalaryDeductionPercent().compareTo(BigDecimal.ZERO) < 0
                || product.getMaximumSalaryDeductionPercent().compareTo(new BigDecimal("100")) > 0) {
            throw new ServiceValidationException("Maximum salary deduction percent must be between 0 and 100");
        }
    }
}
