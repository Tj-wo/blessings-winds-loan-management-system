package com.alganiug.systems.loanManagement.views.converters;

import com.alganiug.systems.loanManagement.core.services.loan.LoanProductService;
import com.alganiug.systems.loanManagement.models.loan.LoanProduct;

import javax.faces.convert.FacesConverter;

@FacesConverter("loanProductConverter")
public class LoanProductConverter extends EntityConverter<LoanProduct> {

    public LoanProductConverter() {
        super("#{loanProductServiceImpl}", LoanProductService.class);
    }
}
