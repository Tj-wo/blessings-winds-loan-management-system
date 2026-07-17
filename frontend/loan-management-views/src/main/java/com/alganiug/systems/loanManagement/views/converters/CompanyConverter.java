package com.alganiug.systems.loanManagement.views.converters;

import com.alganiug.systems.loanManagement.core.services.company.CompanyService;
import com.alganiug.systems.loanManagement.models.company.Company;

import javax.faces.convert.FacesConverter;

@FacesConverter("companyConverter")
public class CompanyConverter extends EntityConverter<Company> {

    public CompanyConverter() {
        super("#{companyServiceImpl}", CompanyService.class);
    }
}
