package com.alganiug.systems.loanManagement.core.services.company.impl;

import com.alganiug.systems.loanManagement.core.services.impl.GenericServiceImpl;
import com.alganiug.systems.loanManagement.core.services.company.CompanyService;
import com.alganiug.systems.loanManagement.models.company.Company;
import com.alganiug.systems.loanManagement.models.constants.AccountStatus;
import com.alganiug.systems.loanManagement.models.constants.RecordStatus;
import com.alganiug.systems.loanManagement.models.security.Role;
import com.alganiug.systems.loanManagement.models.security.RoleConstants;
import com.alganiug.systems.loanManagement.models.security.User;
import com.alganiug.systems.loanManagement.core.services.ServiceValidationException;
import com.alganiug.systems.loanManagement.core.services.security.UserService;
import com.alganiug.systems.loanManagement.utils.PasswordUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
public class CompanyServiceImpl extends GenericServiceImpl<Company> implements CompanyService {

    public CompanyServiceImpl() {
        super(Company.class);
    }

    @Override
    @Transactional
    public Company saveInstance(Company company) {
        boolean creating = company != null && company.getId() == null;
        if (creating) {
            ensureContactAccountCanBeCreated(company);
        }

        Company savedCompany = super.saveInstance(company);
        if (creating) {
            createContactAccount(savedCompany);
        }
        return savedCompany;
    }

    private void ensureContactAccountCanBeCreated(Company company) {
        requireText(company.getContactEmail(), "Contact email");
        String username = company.getContactEmail().trim().toLowerCase(Locale.ROOT);
        boolean exists = !entityManager.createQuery(
                        "select user.id from User user where lower(user.username) = :username", UUID.class)
                .setParameter("username", username)
                .setMaxResults(1)
                .getResultList()
                .isEmpty();
        if (exists) {
            throw new ServiceValidationException("A user account already exists for the company contact email");
        }
    }

    private void createContactAccount(Company company) {
        Role hrRole = entityManager.createQuery(
                        "select role from Role role where role.name = :name and role.recordStatus = :status", Role.class)
                .setParameter("name", RoleConstants.ROLE_HR_SUPERVISOR)
                .setParameter("status", RecordStatus.ACTIVE)
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> new ServiceValidationException("HR Supervisor role is not configured"));

        String username = company.getContactEmail().trim().toLowerCase(Locale.ROOT);
        User user = new User();
        user.setUsername(username);
        user.setEmail(username);
        user.setDisplayName(company.getContactPerson().trim());
        user.setPasswordHash(PasswordUtil.hash(UserService.DEFAULT_COMPANY_CONTACT_PASSWORD));
        user.setAccountStatus(AccountStatus.PENDING);
        user.setCompany(company);
        user.getRoles().add(hrRole);
        entityManager.persist(user);
    }
    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Optional<Company> findByOnboardingCode(String onboardingCode) {
        if (onboardingCode == null || onboardingCode.trim().isEmpty()) {
            return Optional.empty();
        }

        return entityManager.createQuery("select company from Company company where company.onboardingCode = :code and company.recordStatus = :status", Company.class)
                .setParameter("code", onboardingCode.trim())
                .setParameter("status", com.alganiug.systems.loanManagement.models.constants.RecordStatus.ACTIVE)
                .getResultStream()
                .findFirst();
    }

    @Override
    protected void validate(Company company) {
        requireText(company.getName(), "Company name");
        if (company.getCompanyCode() == null || company.getCompanyCode().trim().isEmpty()) {
            company.setCompanyCode("BWC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT));
        }
        if (company.getRegistrationNumber() == null || company.getRegistrationNumber().trim().isEmpty()) {
            company.setRegistrationNumber("BWR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT));
        }
        requireText(company.getContactPerson(), "Contact person");
        requireText(company.getContactEmail(), "Contact email");
        requireText(company.getContactPhone(), "Contact phone");
        if (company.getOnboardingCode() == null || company.getOnboardingCode().trim().isEmpty()) {
            company.setOnboardingCode("join-" + UUID.randomUUID().toString().replace("-", "")
                    .toLowerCase(Locale.ROOT));
        }
        requirePresent(company.getStatus(), "Company status");
    }
}
