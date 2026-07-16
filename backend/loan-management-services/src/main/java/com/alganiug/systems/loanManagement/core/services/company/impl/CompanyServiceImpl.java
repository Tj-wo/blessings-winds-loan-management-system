package com.alganiug.systems.loanManagement.core.services.company.impl;

import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.company.CompanyService;
import com.alganiug.systems.loanManagement.models.company.Company;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl extends GenericServiceImpl<Company> implements CompanyService {

    public CompanyServiceImpl() {
        super(Company.class);
    }

    @Override
    protected void validate(Company company) {
        requireText(company.getCompanyCode(), "Company code");
        requireText(company.getName(), "Company name");
        requireText(company.getRegistrationNumber(), "Registration number");
        requireText(company.getContactPerson(), "Contact person");
        requireText(company.getContactEmail(), "Contact email");
        requireText(company.getContactPhone(), "Contact phone");
        requireText(company.getOnboardingCode(), "Onboarding code");
        requirePresent(company.getStatus(), "Company status");
    }
}
