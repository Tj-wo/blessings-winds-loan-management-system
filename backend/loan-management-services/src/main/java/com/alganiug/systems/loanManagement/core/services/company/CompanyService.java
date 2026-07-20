package com.alganiug.systems.loanManagement.core.services.company;

import com.alganiug.systems.loanManagement.core.services.GenericService;
import com.alganiug.systems.loanManagement.models.company.Company;

import java.util.Optional;

public interface CompanyService extends GenericService<Company> {

    Optional<Company> findByOnboardingCode(String onboardingCode);
}
