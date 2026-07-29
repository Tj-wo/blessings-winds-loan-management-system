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
import com.alganiug.systems.loanManagement.core.services.notification.EmailService;
import com.alganiug.systems.loanManagement.core.services.audit.AuditContext;
import org.springframework.beans.factory.annotation.Autowired;
import com.alganiug.systems.loanManagement.utils.PasswordUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.security.SecureRandom;

@Service
public class CompanyServiceImpl extends GenericServiceImpl<Company> implements CompanyService {

    private static final String PASSWORD_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789!@#$%";
    private final SecureRandom secureRandom = new SecureRandom();
    @Autowired private EmailService emailService;

    public CompanyServiceImpl() {
        super(Company.class);
    }

    @Override
    @Transactional
    public Company saveInstance(Company company) {
        boolean creating = company != null && company.getId() == null;
        if (AuditContext.getActorId() != null) {
            String permission = creating ? "COMPANY_CREATE" : "COMPANY_EDIT";
            if (!actorHasPermission(permission)) throw new ServiceValidationException("You are not allowed to save companies");
            if (!creating && !actorHasRole(RoleConstants.ROLE_ADMINISTRATOR)) {
                UUID companyId = actorCompanyId();
                if (company == null || companyId == null || !companyId.equals(company.getId())) {
                    throw new ServiceValidationException("You cannot edit another company");
                }
            }
        }
        if (creating) {
            ensureContactAccountCanBeCreated(company);
        }

        Company savedCompany = super.saveInstance(company);
        if (creating) {
            createContactAccount(savedCompany);
        }
        return savedCompany;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Company> getInstanceById(UUID id) {
        Optional<Company> company = super.getInstanceById(id);
        if (!company.isPresent() || AuditContext.getActorId() == null
                || actorHasRole(RoleConstants.ROLE_ADMINISTRATOR)) return company;
        UUID companyId = actorCompanyId();
        return companyId != null && companyId.equals(company.get().getId()) ? company : Optional.empty();
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
        String temporaryPassword = temporaryPassword();
        user.setPasswordHash(PasswordUtil.hash(temporaryPassword));
        user.setAccountStatus(AccountStatus.PENDING);
        user.setCompany(company);
        user.getRoles().add(hrRole);
        entityManager.persist(user);
        emailService.send(user.getEmail(), "Your Blessed Winds Loans company account",
                "Your company account has been created.\n\nUsername: " + user.getUsername()
                        + "\nTemporary password: " + temporaryPassword
                        + "\n\nYour account must be activated before sign-in.");
    }

    private String temporaryPassword() {
        StringBuilder password = new StringBuilder(14);
        for (int i = 0; i < 14; i++) password.append(PASSWORD_CHARS.charAt(secureRandom.nextInt(PASSWORD_CHARS.length())));
        return password.toString();
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
