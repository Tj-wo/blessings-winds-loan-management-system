package com.alganiug.systems.loanManagement.views.converters;

import com.alganiug.systems.loanManagement.core.services.loan.LoanService;
import com.alganiug.systems.loanManagement.models.loan.Loan;

import javax.faces.convert.FacesConverter;

@FacesConverter("loanConverter")
public class LoanConverter extends EntityConverter<Loan> {

    public LoanConverter() {
        super("#{loanServiceImpl}", LoanService.class);
    }
}
